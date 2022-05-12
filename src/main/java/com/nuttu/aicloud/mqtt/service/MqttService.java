package com.nuttu.aicloud.mqtt.service;

import com.alibaba.fastjson.JSON;
import com.nuttu.aicloud.model.device.DeviceType;
import com.nuttu.aicloud.model.gateway.Gateway;
import com.nuttu.aicloud.model.status.Status;
import com.nuttu.aicloud.mqtt.model.StatusMessage;
import com.nuttu.aicloud.repository.GatewayRepository;
import com.nuttu.aicloud.repository.StatusRepository;

import com.nuttu.aicloud.webSocket.server.WebSocketServer;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MqttService {

    private static Logger logger = LoggerFactory.getLogger(MqttService.class);
    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    public void handleGWStatus(String gatewayMac, List<StatusMessage> statusMessages) {
        long currentTimeMillis = System.currentTimeMillis();
        List<Point> points_gw = new ArrayList<>();
        List<Point> points_scanner = new ArrayList<>();
        List<String> macs = new ArrayList<>();
        List<Status> statuses = new ArrayList<>();
        List<Status> savedStatuses = new ArrayList<>();
        Map<String, Status> statusMap = new HashMap<>();


        for (int i = 0; i < statusMessages.size(); i++) {
            StatusMessage statusMessage = statusMessages.get(i);
            if ("Gateway".equals(statusMessage.getType())) {
                logger.info("statusMessage(" + i + "): " + statusMessage);
                // Create some data...
                if (statusMessage.getTimestamp() == null){
                    points_gw.add(Point.measurement("Gateway")
                            // .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                            // .time(new Date().getTime(), TimeUnit.MILLISECONDS)
                            .time(System.currentTimeMillis(),TimeUnit.MILLISECONDS)
                            .tag("gatewayMac", gatewayMac).tag("mac", statusMessage.getMac())
                            .addField("name", statusMessage.getName()).build());
                }else{
                    points_gw.add(Point.measurement("Gateway")
                            // .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                            .time(statusMessage.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                            .tag("gatewayMac", gatewayMac).tag("mac", statusMessage.getMac())
                            .addField("name", statusMessage.getName()).build());
                }



            } else if ("Scanner".equals(statusMessage.getType())) {
                points_scanner.add(Point.measurement("Scanner")
                        // .time(currentTimeMillis, TimeUnit.MILLISECONDS)
                        .time(statusMessage.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                        .tag("gatewayMac", gatewayMac).tag("mac", statusMessage.getMac())
                        .addField("name", statusMessage.getName()).build());
            }
            // TODO:: more types to process ...

            if (statusMessages.get(i).getMac() != null) {
                macs.add(statusMessages.get(i).getMac());
            }

        }

        if (points_gw.size() > 0) {
            logger.info("write points_gw");
            logger.info("points_gw:" + points_gw);
            influxDBTemplate.write(points_gw);
        }
        if (points_scanner.size() > 0) {
            influxDBTemplate.write(points_scanner);
        }
        for (int i = 0; i < statusMessages.size(); i++) {
            // System.out.println(statusMessages.get(i).getMac());
            statuses = statusRepository.findAllByGatewayMacAndMacIn(gatewayMac, statusMessages.get(i).getMac());
            StatusMessage statusMessage = statusMessages.get(i);
            Status status = null;
            System.out.println(statuses);
            if (statuses.size() == 0) {
                status = new Status();
                status.copyFrom(gatewayMac, statusMessage);
                statusRepository.save(status);
            } else {
                for (int t = 0; t < statuses.size(); t++) {
                    status = statuses.get(t);
                    // System.out.println(status);
                    if (DeviceType.Gateway.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        statusRepository.save(status);
                        System.out.println("webSocket发送信息");
                        System.out.println(JSON.toJSONString(status));
                        WebSocketServer.sendOneMessage(gatewayMac, JSON.toJSONString(status));
                    } else if (DeviceType.Scanner.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        // System.out.println(status);
                        statusRepository.save(status);
                    } else if (DeviceType.Unknown.equals(status.getType())) {
                        status.copyFrom(gatewayMac, statusMessage);
                        statusRepository.save(status);

                    }
                }
            }
        }
    }

    public void handleGWRegister(String gatewayMac, Gateway gateway) {
        Boolean exist = gatewayRepository.existsByMac(gatewayMac);
        Gateway gw = gatewayRepository.findOneByMac(gatewayMac).orElse(new Gateway());
        gw.copyContent(gateway);

        if (!exist) {
            gw.setActive(true); // 如果第一次注册，缺省激活，否则的话保持当前状态
        }

        logger.info("gateway to save: " + gw);
        Gateway gw2 = null;
        try {
            gw2 = gatewayRepository.save(gw);
        }catch (Exception e) {
            System.out.println("存入数据库失败:"+gatewayMac);
            e.printStackTrace();
        }


        try {
            String resp = "gateway " + (exist?"added":"updated") + " " + (gw2!=null?"successfully":"failed") + ".";
            MqttBroker.publish("/ai/" + gatewayMac + "/reg_resp", resp);
        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }
}
