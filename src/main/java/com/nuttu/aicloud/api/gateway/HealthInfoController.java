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
    @ApiOperation(value = "Gets HealthCodes' information", response = Page.class)
    @RequestMapping(value="/healthInfos", method= RequestMethod.GET)
    public PageResponse getHealthInfoList(
            @ApiParam(value = "" ) @RequestParam(value = "owner"  ,  required = false) String owner,
            @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "00", required = false) Integer size,
            @ApiParam(value = "") @RequestParam(value = "sortType", defaultValue = "DESC", required = false) String sortType,
            @ApiParam(value = "") @RequestParam(value = "sortFields", defaultValue = "updatedAt", required = false) String sortFields
            ) {
        Sort sort = "DESC".equals(sortType)?new Sort(Sort.Direction.DESC,sortFields):new Sort(Sort.Direction.ASC,sortFields);
        Pageable pageable = new PageRequest(page,size, sort);
        PageResponse resp = new PageResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        System.out.println(loggedInUserId);
        Page<HealthInfo> r;

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
//                    System.out.println("return all infos");
                    r = healthInfoRepository.findAll(pageable);
                }else{
                    System.out.println("根据表单信息查询");
                    r = healthInfoRepository.findByOwner(owner,pageable);
                }
            }else{
                System.out.println("权限不足根据登陆用户查询");
                r = healthInfoRepository.findByOwner(user.getEmail(), pageable);
            }
            resp.setPageStats(r,true);
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
            HealthInfo r = healthInfoRepository.findOneByUuid(uuid).orElse(null);
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
        HealthInfo hi = null;
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
            if(updateMsgAck.getCode()==21){
                DeviceConfigRequest deviceConfigRequest = new DeviceConfigRequest();
                DeviceConfig deviceConfig =  deviceConfigRepository.findOneById(1).orElse(null);
                if(deviceConfig!=null){
                    deviceConfigRequest.setTransaction(sn.hashCode());
                    deviceConfigRequest.setNew_version(deviceConfig.getVersion());
                    deviceConfigRequest.setUpg_url(deviceConfig.getUpg_path());
                    resp.setData(deviceConfigRequest);
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
            @ApiParam(value = "id" ) @RequestParam(value="id",required = false) Integer id,
            @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
            @ApiParam(value = "") @RequestParam(value = "sort", defaultValue = "updatedAt", required = false) String sort,
            Pageable pageable
    ){
        OperationResponse resp = new OperationResponse();
        DeviceConfig r;
        id = 1;
        r  = deviceConfigRepository.findOneById(id).orElse(null);
        if(r!=null){
            resp.setStatus(200);
            resp.setData(r);
        }else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;

    }

    @ApiOperation(value = "修改设备配置",response = OperationResponse.class)
    @RequestMapping(value = "/healthInfos/config",method=RequestMethod.PUT)
    public OperationResponse postConfig(@RequestBody DeviceConfig deviceConfig){
        OperationResponse resp = new OperationResponse();
        DeviceConfig dc = null;
        dc = deviceConfigRepository.findOneById(1).orElse(null);
        if(dc != null){
            deviceConfig.setId(1);
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
    @RequestMapping(value = "healthInfos/download/{name}")
    public void fileDownload(@PathVariable String name, HttpServletResponse response){
        try {
            new DownloadService().logDownload(name,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
