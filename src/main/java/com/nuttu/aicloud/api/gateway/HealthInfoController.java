package com.nuttu.aicloud.api.gateway;

import com.nuttu.aicloud.api.user.UserService;
import com.nuttu.aicloud.model.gateway.*;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.model.response.PageResponse;
import com.nuttu.aicloud.model.user.Role;
import com.nuttu.aicloud.model.user.User;
import com.nuttu.aicloud.repository.DeviceConfigRepository;
import com.nuttu.aicloud.repository.GatewayRepository;
import com.nuttu.aicloud.repository.HealthInfoRepository;
import com.nuttu.aicloud.util.FileTransfer.DownloadService;
import com.nuttu.aicloud.util.FileTransfer.UploadService;
import com.nuttu.aicloud.util.FileTransfer.DeleteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wing
 */
@RestController
@Api(tags = {"HealthInfo"})
public class HealthInfoController {

    @Autowired
    private HealthInfoRepository healthInfoRepository;
    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private DeviceConfigRepository deviceConfigRepository;
    @Autowired
    UserService userService;

    @ApiOperation(value = "Gets HealthCodes' simple information", response = Page.class)
    @RequestMapping(value="/healthInfos", method= RequestMethod.GET)
    public PageResponse getHealthInfoList(
            @ApiParam(value = "" ) @RequestParam(value = "owner"  ,  required = false) String owner,
            @ApiParam(value = "" ) @RequestParam(value = "sn"  ,defaultValue = "%%",  required = false) String sn,
            @ApiParam(value = "" ) @RequestParam(value = "code_color"  ,defaultValue = "%%",  required = false) String code_color,
            @ApiParam(value = "" ) @RequestParam(value = "type"  ,defaultValue = "%%",  required = false) String type,
            @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
            @ApiParam(value = "") @RequestParam(value = "sortType", defaultValue = "DESC", required = false) String sortType,
            @ApiParam(value = "") @RequestParam(value = "sortFields", defaultValue = "updatedAt", required = false) String sortFields
    ) {
        Sort sort = "DESC".equals(sortType)?new Sort(Sort.Direction.DESC,sortFields):new Sort(Sort.Direction.ASC,sortFields);
        Pageable pageable = new PageRequest(page,size, sort);
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();
        Page<HealthIndoModel> r;

        if(!sn.equals("%%")) sn = "%"+sn+"%";
        if(!type.equals("%%")) type = "%"+type+"%";
//        System.out.println(pageable);
        if(Objects.equals(loggedInUserId, "")){
            resp.setStatus(400);
            resp.setMessage("Current user does not exist");
        }else{
            User user = userService.getLoggedInUser();
            if(Role.ADMIN == user.getRole()){
//                System.out.println(owner);
//                System.out.println(sort);
                if(owner==null||owner.length()==0){
                    System.out.println("return all infos");
                    if(sn!="%%"||code_color!="%%"||type!="%%"){
                        r = healthInfoRepository.findPartByParams("%%",sn,code_color,type,pageable);
                    }else{
                        r = healthInfoRepository.findAllPart(pageable);
                    }
                }else{
                    System.out.println("根据表单信息查询");
                    r = healthInfoRepository.findPartByParams(owner,sn,code_color,type,pageable);
                }
            }else{
                System.out.println("权限不足根据登陆用户查询");
                if(sn!="%%"||code_color!="%%"||type!="%%"){
                    r = healthInfoRepository.findPartByParams(user.getEmail(),sn,code_color,type,pageable);
                }else{
                    r = healthInfoRepository.findPartByOwner(user.getEmail(), pageable);
                }
            }
            resp.setPageStats(r,true);
        }
        return resp;
    }
    //


    @ApiOperation(value = "Gets HealthCodes' simple information", response = OperationResponse.class)
    @RequestMapping(value="/healthInfos/{uuid}", method= RequestMethod.GET)
    public OperationResponse getHealthInfoByUuid(@PathVariable String uuid){
        OperationResponse resp = new OperationResponse();
        if(uuid!=null){
            HealthIndoModel oneByUuid = healthInfoRepository.findOneByUuid(uuid).orElse(null);
            if(oneByUuid!=null){
                resp.setStatus(200);
                resp.setMessage("成功得到健康码信息");
                resp.setData(oneByUuid);
            }else{
                resp.setStatus(415);
                resp.setMessage("未找到相关信息");
            }
        }else{
            resp.setStatus(410);
            resp.setMessage("请求信息错误");
        }
        return resp;
    }
    @ApiOperation(value = "Add new health infomation",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos",method = RequestMethod.POST)
    public OperationResponse postHealthInfo(@RequestBody HealthInfo healthInfo, HttpServletRequest req){
        OperationResponse resp = new OperationResponse();
        if("" == healthInfo.getOwner() ||"".equals(healthInfo.getOwner())||"".equals(healthInfo.getType())||"".equals(healthInfo.getCode_color())){
            resp.setStatus(400);
            resp.setMessage("表单不完整，新建失败");
        }else{
            Optional<Gateway> gateway = gatewayRepository.findOneByMac(healthInfo.getSn());
            if(gateway.isPresent()){
                HealthInfo HI =  healthInfoRepository.save(healthInfo);
                resp.setStatus(200);
                resp.setMessage("Successfully received information");
                resp.setData(HI);
            }else{
                resp.setStatus(450);
                resp.setMessage("未找到该设备");
            }
        }
        return resp;
    }

    @ApiOperation(value = "Delete healthInfo by uuid",response = OperationResponse.class)
    @RequestMapping(value="/healthInfos/{uuid}",method=RequestMethod.DELETE)
    public OperationResponse deleteHealthInfo(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();

        if(this.healthInfoRepository.exists(uuid)){
            HealthIndoModel r = healthInfoRepository.findOneByUuid(uuid).orElse(null);
            this.healthInfoRepository.delete(uuid);
            resp.setStatus(200);
            resp.setMessage("Record Deleted");
            resp.setData(r);
        }
        else{
            resp.setStatus(400);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }

    @ApiOperation(value = "Edit gateway information by uuid",response = OperationResponse.class)
    @RequestMapping(value="/healthInfos/{uuid}",method=RequestMethod.PUT)
    public OperationResponse putHealthInfo(@PathVariable String uuid,@RequestBody HealthInfo healthInfo){
        OperationResponse resp = new OperationResponse();
        HealthIndoModel hi = null;
        if(uuid!=null){
            hi = healthInfoRepository.findOneByUuid(uuid).orElse(null);
        }
        if(hi!=null){
            healthInfo.setOwner(hi.getOwner());
            healthInfo.setSn(hi.getSn());
            healthInfo.setUuid(hi.getUuid());
            HealthInfo r = this.healthInfoRepository.save(healthInfo);
            resp.setStatus(200);
            resp.setMessage("Record Updated");
            resp.setData(r);
        }else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }

    @ApiOperation(value = "设备状态定期报告",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/report",method=RequestMethod.POST)
    public OperationResponse postReport(@RequestBody HealthInfoStatus healthInfoStatus){
        OperationResponse resp = new OperationResponse();
        Optional<Gateway> gateway = gatewayRepository.findOneByMac(healthInfoStatus.getSn());

        if(gateway.isPresent()){
            resp.setStatus(200);
            resp.setMessage("成功接收消息");
            Optional<DeviceConfig> deviceConfig = deviceConfigRepository.findOneById(1);
            if(deviceConfig.isPresent()){
                DeviceConfig config = deviceConfig.get();
                if(config.getBoardUpdate()==1||config.getBoardConfig()==1){
                    DeviceConfigRequest deviceConfigRequest = new DeviceConfigRequest();
                    deviceConfigRequest.setTransaction(healthInfoStatus.getSn().hashCode());
                    if(config.getBoardUpdate()==1){
                        if(Objects.equals(config.getVersion(), gateway.get().getVersion())){
                            resp.setMessage("成功接收消息，设备已是最新版本");
                        }else{
                            deviceConfigRequest.setNew_version(config.getVersion());
                            deviceConfigRequest.setUpg_url(config.getUpg_path());
//                            gateway.get().setVersion(config.getVersion());
//                            gatewayRepository.save(gateway.get());
                        }
                    }
                    if(config.getBoardConfig()==1){
                        deviceConfigRequest.setConfig(config.getConfig());
                    }
                    resp.setData(deviceConfigRequest);
                }

            }else{
                //还不知道要咋办。。再说吧，应该不会发生的
                //TODO::异常处理
            }
        }else{
            resp.setStatus(450);
            resp.setMessage("Can't find device");
        }
        return resp;
    }

    @ApiOperation(value = "设备状态定期报告",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/reportack",method=RequestMethod.POST)
    public OperationResponse postReport(@RequestBody UpdateMsgAck updateMsgAck) {
        OperationResponse resp = new OperationResponse();
        String sn = updateMsgAck.getSn();
        Gateway gw = gatewayRepository.findOneByMac(sn).orElse(null);
        if(gw!=null){
            resp.setStatus(200);
            DeviceConfig deviceConfig =  deviceConfigRepository.findOneById(1).orElse(null);
            if(updateMsgAck.getCode()==21){
                DeviceConfigRequest deviceConfigRequest = new DeviceConfigRequest();
                if(deviceConfig!=null){
                    deviceConfigRequest.setTransaction(sn.hashCode());
                    deviceConfigRequest.setNew_version(deviceConfig.getVersion());
                    deviceConfigRequest.setUpg_url(deviceConfig.getUpg_path());
                    resp.setData(deviceConfigRequest);
                }
            }else if(updateMsgAck.getCode()==6){
                if (deviceConfig != null) {
                    gw.setVersion(deviceConfig.getVersion());
                    gatewayRepository.save(gw);
                    resp.setMessage("收到消息，设备信息已更新");
                }
            }
        }else{
            resp.setStatus(404);
            resp.setMessage("未找到该设备");
        }

        return resp;
    }

    @ApiOperation(value = "获取配置信息",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/config",method = RequestMethod.GET)
    public  OperationResponse getConfig(
//            @ApiParam(value = "id" ) @RequestParam(value="owner",required = false) String owner

    ){
        OperationResponse resp = new OperationResponse();
        DeviceConfig r;
        String owner;
        String loggedInUserId = userService.getLoggedInUserId();
        owner = userService.getUserByUuid(loggedInUserId).getEmail();

        System.out.println(owner);
        r = deviceConfigRepository.findOneByOwner(owner).orElseGet(() -> new DeviceConfig());
        if(!Objects.equals(r.getOwner(), "")){
            System.out.println("有记录");
            resp.setStatus(200);
            resp.setData(r);
        }else{
            r.setOwner(owner);
            DeviceConfig f = deviceConfigRepository.save(r);
            System.out.println(f);
            if(f!=null){
                resp.setStatus(200);
                resp.setData(f);
            }else{
                resp.setStatus(404);
                resp.setMessage("No Record Exist");
            }
        }
        return resp;

    }

    @ApiOperation(value = "修改设备配置",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/config",method=RequestMethod.PUT)
    public OperationResponse postConfig(@RequestBody DeviceConfig deviceConfig){
        OperationResponse resp = new OperationResponse();
        DeviceConfig dc = null;
        String loggedInUserId = userService.getLoggedInUserId();
        String owner = userService.getUserByUuid(loggedInUserId).getEmail();

        dc = deviceConfigRepository.findOneByOwner(owner).orElse(null);
        if(dc != null) {
            DeviceConfig r = this.deviceConfigRepository.save(deviceConfig);
            resp.setStatus(200);
            resp.setMessage("Record Update");
            resp.setData(r);
        }else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }



    @ApiOperation(value = "上传固件更新包",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/upload",method=RequestMethod.POST)
    public OperationResponse postUpload(@RequestParam("file") MultipartFile file)throws Exception{
        return new UploadService().Upload(file);
    }

    @ApiOperation(value = "下载升级包")
    @GetMapping(value = "healthInfos/download")
    public void fileDownload(@RequestParam(value="name") String name, HttpServletResponse response){
        try {
            new DownloadService().logDownload(name,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "删除文件")
    @RequestMapping(value = "/healthInfos/delete",method=RequestMethod.POST)
    public OperationResponse fileDelete(@RequestParam(value="name") String fileName){
        OperationResponse resp = new OperationResponse();
        if(new DeleteService().Delete(fileName)){
            resp.setStatus(200);
            resp.setMessage("成功删除文件:"+fileName);
        }else{
            resp.setStatus(404);
            resp.setMessage("删除文件失败:"+fileName);
        }
        return resp;
    }



}
