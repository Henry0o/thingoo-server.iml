package com.nuttu.aicloud.api.gateway;

import com.nuttu.aicloud.api.user.UserService;
import com.nuttu.aicloud.model.gateway.Gateway;
import com.nuttu.aicloud.model.gateway.HealthCode;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.model.response.PageResponse;
import com.nuttu.aicloud.repository.GatewayRepository;
import com.nuttu.aicloud.util.QRCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@Api(tags = {"Gateway"})
public class GatewayController {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Gets gateways' information", response = Page.class)
    @RequestMapping(value="/gateways", method= RequestMethod.GET)
    public PageResponse getGatewayList(
        @ApiParam(value = "" ) @RequestParam(value = "user"  ,  required = false) String user,
        @ApiParam(value = "") @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
        @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size,
        @ApiParam(value = "") @RequestParam(value = "sort", defaultValue = "updatedAt", required = false) String sort,
        Pageable pageable) {
        PageResponse resp = new PageResponse();
        //String loggedInUserId = userService.getLoggedInUserId();

        Page<Gateway> r;

        if (user != null && !user.isEmpty()){
            r = gatewayRepository.findByUserId(user, pageable);
        } else {
            //r = gatewayRepository.findByUserId(loggedInUserId, pageable);
            r = gatewayRepository.findAll(pageable);    //暂时先管理全部了，权限控制问题将来再加
        }
        resp.setPageStats(r,true);

        return resp;
    }



    @ApiOperation(value = "Add new healthCode", response = OperationResponse.class)
    @RequestMapping(value = "/gateways/addHealthCode", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postGateway(@RequestBody HealthCode healthCode, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        logger.info("得到健康码设备注册请求"+healthCode.toString());
        String loggedInUserId = userService.getUuidByEmail(healthCode.getOwner());
//        resp.setLoggedInUserId(loggedInUserId);
        List<Gateway> gateway1= gatewayRepository.findByMac(healthCode.getSn());
        if(("").equals(loggedInUserId)){
            resp.setStatus(415);
            resp.setMessage("User does not exist");
            logger.info("健康码设备注册失败，用户不存在");
        }else{
            if (gateway1.size()==0){
                //如果数据库中没有该设备
                Gateway gateway = new Gateway();
                gateway.setUserId(loggedInUserId);
                gateway.setMac(healthCode.getSn());
                gateway.setVersion(healthCode.getVersion());
                gateway.setAddress(healthCode.getAddress());
                gateway.setAdmin_contact(healthCode.getPhone());
                gateway.setAdmin_name(healthCode.getContact());
                gateway.setActive(true);
                Gateway r = gatewayRepository.save(gateway);
                if (r.getUuid() != "") {
                    resp.setStatus(200);
                    resp.setMessage("Record Added");
                    logger.info("健康码设备注册成功,设备序列号为"+r.getMac());
                    resp.setData(r);
                } else {
                    resp.setStatus(400);
                    resp.setMessage("Unable to add Record");
                    logger.info("健康码设备注册失败，存入数据库失败"+r.getMac());
                }
            }else {
                resp.setStatus(450);
                resp.setMessage("The gateway has been added");
                logger.info("健康码设备注册失败，用户未找到"+healthCode.getOwner());
            }
        }

        return resp;
    }
    @ApiOperation(value = "Add new gateway", response = OperationResponse.class)
    @RequestMapping(value = "/gateways", method = RequestMethod.POST, produces = {"application/json"})
    public OperationResponse postGateway(@RequestBody Gateway gateway, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);
        List<Gateway> gateway1= gatewayRepository.findByMac(gateway.getMac());
        if (gateway1.size()==0){
            //如果数据库中没有该设备
            gateway.setUserId(loggedInUserId);
            gateway.setActive(true);
            Gateway r = gatewayRepository.save(gateway);
            if (r.getUuid() != "") {
                resp.setStatus(200);
                resp.setMessage("Record Added");
                resp.setData(r);
            } else {
                resp.setStatus(400);
                resp.setMessage("Unable to add Record");
            }
        }else {
            resp.setStatus(450);
            resp.setMessage("The gateway has been added");
        }
        return resp;
    }

    @ApiOperation(value = "Gets gateway information by uuid", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.GET, produces = {"application/json"})
    public OperationResponse getGateway(@PathVariable String uuid, HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        Gateway gateway = null;
        if (uuid != null){
            gateway = gatewayRepository.findOneByUuid(uuid).orElse(null);
            if (gateway == null)
                gateway = gatewayRepository.findOneByMac(uuid).orElse(null);
        }

        if (gateway != null) {
            resp.setStatus(200);
            resp.setData(gateway);
        } else {
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Edit gateway information by uuid", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.PUT)
    public OperationResponse putGateway(@PathVariable String uuid, @RequestBody Gateway gateway) {
        OperationResponse resp = new OperationResponse();
//        String loggedInUserId = userService.getLoggedInUserId();
//        resp.setLoggedInUserId(loggedInUserId);

        Gateway gw = null;
        if (uuid != null){
            gw = gatewayRepository.findOneByUuid(uuid).orElse(null);
            if (gw == null)
                gw = gatewayRepository.findOneByMac(uuid).orElse(null);
        }
        if (gw != null){
            gateway.setUuid(gw.getUuid());
            gateway.setMac(gw.getMac());
            gateway.setStatus(gw.getStatus());
            gateway.setActive(gw.isActive());
            gateway.setUserId(gw.getUserId());
            Gateway r = this.gatewayRepository.save(gateway);
            resp.setStatus(200);
            resp.setMessage("Record Updated");
            resp.setData(r);
        }
        else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }

        return resp;
    }

    @ApiOperation(value = "Activate gateway by uuid", response = OperationResponse.class)
    @RequestMapping(value="/gateways/activate", method=RequestMethod.GET)
    public OperationResponse activateGateway(HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();

        String uuid = req.getParameter("mac");
        if(uuid==null){
            uuid = req.getParameter("uuid");
        }

        Gateway gw = null;
        if (uuid != null) {
            gw = gatewayRepository.findOneByUuid(uuid).orElse(null);
            if (gw == null){
                gw = gatewayRepository.findOneByMac(uuid).orElse(null);
            }
        }
        if(gw!=null){
            if(gw.isActive()){
                resp.setStatus(450);
                resp.setMessage("设备已被激活，无需重复操作");
            }else{
                gw.setActive(true);
                Gateway r = this.gatewayRepository.save(gw);
                resp.setStatus(200);
                resp.setMessage("已成功激活设备");
                resp.setData(r);
            }
        }else{
            resp.setStatus(404);
            resp.setMessage("未找到该设备");
        }
        return resp;
    }
    @ApiOperation(value = "Deactivate gateway by uuid", response = OperationResponse.class)
    @RequestMapping(value="/gateways/deactivate", method=RequestMethod.GET)
    public OperationResponse deactivateGateway(HttpServletRequest req) {
        OperationResponse resp = new OperationResponse();

        String uuid = req.getParameter("mac");
        if(uuid==null){
            uuid = req.getParameter("uuid");
        }

        Gateway gw = null;
        if (uuid != null) {
            gw = gatewayRepository.findOneByUuid(uuid).orElse(null);
            if (gw == null){
                gw = gatewayRepository.findOneByMac(uuid).orElse(null);
            }
        }
        if(gw!=null){
            if(!gw.isActive()){
                resp.setStatus(450);
                resp.setMessage("设备已被停用，无需重复操作");
            }else{
                gw.setActive(false);
                Gateway r = this.gatewayRepository.save(gw);
                resp.setStatus(200);
                resp.setMessage("已成功停用设备");
                resp.setData(r);
            }
        }else{
            resp.setStatus(404);
            resp.setMessage("未找到该设备");
        }
        return resp;
    }

    @ApiOperation(value = "Delete gateway by uuid", response = OperationResponse.class)
    @RequestMapping(value="/gateways/{uuid}", method=RequestMethod.DELETE)
    public OperationResponse deleteGateway(@PathVariable String uuid) {
        OperationResponse resp = new OperationResponse();
        String loggedInUserId = userService.getLoggedInUserId();

        if (this.gatewayRepository.exists(uuid) ){
            Gateway r =gatewayRepository.findOneByUuid(uuid).orElse(null);
            this.gatewayRepository.delete(uuid);
            resp.setStatus(200);
            resp.setMessage("Record Deleted");
            resp.setData(r);
        }
        else{
            resp.setStatus(404);
            resp.setMessage("No Record Exist");
        }
        return resp;
    }
    @SneakyThrows
    @ApiOperation(value = "Create QRCode", response = OperationResponse.class)
    @RequestMapping(value= "/gateways/getQrCode",method=RequestMethod.GET)
    public OperationResponse getQrCode(@RequestParam String context) {
        OperationResponse resp = new OperationResponse();
        BufferedImage bufferedImage =  QRCodeUtil.createQrCodeImage(context,300,300);
        String base64IMG = QRCodeUtil.BufferedImageToBase64(bufferedImage);
        if(base64IMG!=""){
            resp.setStatus(200);
            resp.setMessage("Success to create QRCode");
            resp.setData(base64IMG);
            System.out.println("成功生成QR:"+context);
        }else{
            resp.setStatus(404);
            resp.setMessage("Fail to create QRCode");
            System.out.println("生成QR失败:"+context);
        }


        return resp;
    }
}
