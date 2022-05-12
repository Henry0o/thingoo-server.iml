package com.nuttu.aicloud.api.MQTT.gw;

import com.alibaba.fastjson.JSON;
import com.nuttu.aicloud.model.instructions.Instructions;
import com.nuttu.aicloud.model.operation.Operation;
import com.nuttu.aicloud.mqtt.service.MqttBroker;
import com.nuttu.aicloud.repository.OperationRepository;

import jdk.nashorn.internal.ir.ReturnNode;
import org.eclipse.paho.client.mqttv3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestartController {
    @Autowired
    private OperationRepository operationRepository;
    @RequestMapping(value = "account/gatewayMac", method = RequestMethod.POST)
    public String restart(@RequestParam String gatewayMac) throws MqttException {

        //新建一个指令说明
        Instructions instructions = new Instructions();
        instructions.setAction("reboot");
        instructions.setRequestId(gatewayMac);
        System.out.println(instructions.toString());
        String topic = "/gw/" + gatewayMac + "/action";
        System.out.println(topic);
        String o = JSON.toJSONString(instructions);
        MqttBroker.publish(topic, o);
        System.out.println("ww" + o);

        Operation op = new Operation();
        //TODO:: operation info.
        op.setGateway_mac(gatewayMac);
        op.setOperation("reboot");
        MqttBroker.publish(topic,JSON.toJSONString(op));
        operationRepository.save(op);
        return "ok";
//        return gatewayMac;
    }
}
