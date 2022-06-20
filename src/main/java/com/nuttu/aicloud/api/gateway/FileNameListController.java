package com.nuttu.aicloud.api.gateway;

import com.nuttu.aicloud.model.fileNameList.fileNameList;
import com.nuttu.aicloud.model.operation.Operation;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.model.response.PageResponse;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.repository.FileNameListRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author wing
 */
@RestController
@Api(tags = {"FileNameList"})
public class FileNameListController {

    @Autowired
    private FileNameListRepository fileNameListRepository;

    private Specification<fileNameList> getWhereClause(String fileName){
        return new Specification<fileNameList>() {
            @Override
            public Predicate toPredicate(Root<fileNameList> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                if(!fileName.isEmpty()){
                    predicate.getExpressions().add(
                            criteriaBuilder.like(root.<String>get("fileName"), "%" + fileName + "%")
                    );
                }
                return predicate;
            }
        };
    }

    @ApiOperation(value = "Get FileNameList's information",response = Page.class)
    @RequestMapping(value="/fileNameList",method = RequestMethod.GET)
    public PageResponse getFileNameList(
            @ApiParam(value = "" ) @RequestParam(value = "fileName"  ,defaultValue = "",  required = false) String fileName,
            @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
            @ApiParam(value = "") @RequestParam(value = "sortType", defaultValue = "DESC", required = false) String sortType,
            @ApiParam(value = "") @RequestParam(value = "sortFields", defaultValue = "updatedAt", required = false) String sortFields
    ){
        PageResponse resp = new PageResponse();
        Page<fileNameList> r;
        Sort sort = "DESC".equals(sortType)?new Sort(Sort.Direction.DESC,sortFields):new Sort(Sort.Direction.ASC,sortFields);
        Pageable pageable = new PageRequest(page,size,sort);
        if(fileName.isEmpty()){
            r = fileNameListRepository.findAll(pageable);
        }else{
            r = fileNameListRepository.findAll(Specifications.where(getWhereClause(fileName)),pageable);
        }
        resp.setPageStats(r,true);
        return resp;
    }

    @ApiOperation(value = "Add new File", response = OperationResponse.class)
    @RequestMapping(value = "/fileNameList", method = RequestMethod.POST, produces = { "application/json" })
    public OperationResponse postFile(@RequestBody fileNameList file){
        OperationResponse resp = new OperationResponse();
        if(this.fileNameListRepository.existsByfileName(file.getFileName())) {
            resp.setStatus(400);
            resp.setMessage("Fail has been existed");
        }else{
            fileNameList f = fileNameListRepository.save(file);
            if(f.getId()!=null){
                resp.setStatus(200);
                resp.setMessage("Record Added");
                resp.setData(f);
            }else{
                resp.setStatus(400);
                resp.setMessage("Fail to add Recode");
            }
        }
        return resp;
    }
    @ApiOperation(value = "Edit current file information", response = OperationResponse.class)
    @RequestMapping(value = "/fileNameList/{id}", method = RequestMethod.PUT)
    public OperationResponse putFile(@PathVariable Integer id,@RequestBody fileNameList file){
        OperationResponse resp = new OperationResponse();
        fileNameList f = null;
        if(id!=null){
            f = fileNameListRepository.findOneById(id).orElse(null);
        }
        if(f!=null){
            file.setFileName(f.getFileName());
            fileNameList r = this.fileNameListRepository.save(file);
            resp.setStatus(200);
            resp.setMessage("修改成功");
            resp.setData(r);
        }else{
            resp.setStatus(404);
            resp.setMessage("无此用户");
        }
        return resp;
    }

    @ApiOperation(value = "Delete fileNameList by id",response = OperationResponse.class)
    @RequestMapping(value="/fileNameList/{id}",method=RequestMethod.DELETE)
    public OperationResponse deleteFile(@PathVariable Integer id){
        OperationResponse resp = new OperationResponse();
        System.out.println(id);
        if(this.fileNameListRepository.existsById(id)){
            fileNameList f = fileNameListRepository.findOneById(id).orElse(null);
            System.out.println(f);
            this.fileNameListRepository.delete(f);
            resp.setStatus(200);
            resp.setMessage("成功删除");
            resp.setData(f);

        }else{
            resp.setStatus(400);
            resp.setMessage("未查询到该文件id");
        }
        return resp;
    }


}
