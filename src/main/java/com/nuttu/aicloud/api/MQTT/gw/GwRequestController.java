package com.nuttu.aicloud.api.MQTT.gw;


import com.alibaba.fastjson.JSON;
import com.nuttu.aicloud.model.instructions.Instructions;
import com.nuttu.aicloud.model.mqtt.*;
import com.nuttu.aicloud.model.mqtt.RequestBody.ExposureParam;
import com.nuttu.aicloud.model.mqtt.RequestBody.blocklocation;
import com.nuttu.aicloud.model.response.OperationResponse;
import com.nuttu.aicloud.mqtt.service.MqttBroker;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;

/**
 * @author wing
 */
@RestController
public class GwRequestController {

    @RequestMapping(value = "/account/gateway/verquery", method = RequestMethod.POST)
    public OperationResponse gwVerQuery(@RequestParam String gatewayMac) {

        OperationResponse resp = new OperationResponse();
        VersionQueryRequest versionQueryRequest = new VersionQueryRequest();
        String topic = "/ai/" + gatewayMac + "/ver_query";

        try {
            MqttBroker.publish(topic, JSON.toJSONString(versionQueryRequest));
            resp.setStatus(200);
            resp.setMessage("Send Version Query Request success");
            resp.setData(JSON.toJSONString(versionQueryRequest));
        } catch (MqttException e) {
            resp.setStatus(400);
            resp.setMessage("Send Version Query Request fail");
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(versionQueryRequest));
        return resp;
    }

    @RequestMapping(value = "/account/gateway/setfps", method = RequestMethod.POST)
    public OperationResponse gwSetFps(@RequestParam String gatewayMac,
                           @RequestParam int fps) {
        OperationResponse resp = new OperationResponse();
        fpsSetRequest fpsSetRequest = new fpsSetRequest(fps);
        String topic = "/ai/" + gatewayMac + "/set_fps";

        try {
            MqttBroker.publish(topic, JSON.toJSONString(fpsSetRequest));
            resp.setStatus(200);
            resp.setMessage("Send FPS Set Request success");
            resp.setData(JSON.toJSONString(fpsSetRequest));
        } catch (MqttException e) {
            resp.setStatus(400);
            resp.setMessage("Send FPS Set Request fail");
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(fpsSetRequest));

//        return "发送视频帧率设置请求成功";
        return resp;
    }

    @RequestMapping(value = "/account/gateway/setlocation", method = RequestMethod.POST)
    public OperationResponse gwSetLocation(@RequestParam String gatewayMac,
                                @RequestBody blocklocation blocklocation) {
        OperationResponse resp = new OperationResponse();
        String topic = "/ai/" + gatewayMac + "/set_location";
        BlockLocationSetRequest blockLocationSetRequest = new BlockLocationSetRequest(blocklocation.getX(), blocklocation.getY(), blocklocation.getWith(), blocklocation.getHigh());
        try {
            MqttBroker.publish(topic, JSON.toJSONString(blockLocationSetRequest));
            resp.setStatus(200);
            resp.setMessage("Send Block Location Request success");
            resp.setData(JSON.toJSONString(blockLocationSetRequest));
        } catch (MqttException e) {
            e.printStackTrace();
            resp.setStatus(400);
            resp.setMessage("Send Block Location Request fail");
        }
        System.out.println(JSON.toJSONString(blockLocationSetRequest));
        return resp;
    }

    @RequestMapping(value = "/account/gateway/expcapquery", method = RequestMethod.POST)
    public OperationResponse gwExposureCapQuery(@RequestParam String gatewayMac) {
        OperationResponse resp = new OperationResponse();
        String topic = "/ai/" + gatewayMac + "/expcap_query";
        DevExposureCapQueryRequest devExposureCapQueryRequest = new DevExposureCapQueryRequest();

        try {
            MqttBroker.publish(topic, JSON.toJSONString(devExposureCapQueryRequest));
            resp.setStatus(200);
            resp.setMessage("Send Exposure Capture Query success");
            resp.setData(JSON.toJSONString(devExposureCapQueryRequest));
        } catch (MqttException e) {
            resp.setStatus(400);
            resp.setMessage("Send Exposure Capture Query fail");
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(devExposureCapQueryRequest));
        return resp;
    }

    @RequestMapping(value = "/account/gateway/expparamset", method = RequestMethod.POST)
    public OperationResponse gwExpParamSet(@RequestParam String gatewayMac,
                                @RequestBody ExposureParam exposureParam) {
        OperationResponse resp = new OperationResponse();
        String topic = "/ai/" + gatewayMac + "/expparam_set";
        DevExposureParamSetRequest devExposureParamSetRequest = new DevExposureParamSetRequest(exposureParam.getExp(), exposureParam.getDgain(), exposureParam.getAgain());

        try {
            MqttBroker.publish(topic, JSON.toJSONString(devExposureParamSetRequest));
            resp.setStatus(200);
            resp.setMessage("Send Exposure Param Set Request success");
            resp.setData(JSON.toJSONString(devExposureParamSetRequest));
        } catch (MqttException e) {
            resp.setStatus(400);
            resp.setMessage("Send Exposure Param Set Request fail");
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(devExposureParamSetRequest));
        return resp;
    }

    @RequestMapping(value = "/account/gateway/optexpset", method = RequestMethod.POST)
    public OperationResponse gwOptimalExpParamSet(@RequestParam String gatewayMac,
                                       @RequestBody ExposureParam exposureParam) {
        OperationResponse resp = new OperationResponse();
        String topic = "/ai/" + gatewayMac + "/optexp_set";
        OptimalExposireParamSetRequest oeps = new OptimalExposireParamSetRequest(exposureParam.getExp(), exposureParam.getDgain(), exposureParam.getAgain());

        try {
            MqttBroker.publish(topic, JSON.toJSONString(oeps));
            resp.setStatus(200);
            resp.setMessage("Send Optimal Exposure Param Set Request success");
            resp.setData(JSON.toJSONString(oeps));
        } catch (MqttException e) {
            resp.setStatus(400);
            resp.setMessage("Send Optimal Exposure Param Set Request fail");
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(oeps));
        return resp;

    }
}
