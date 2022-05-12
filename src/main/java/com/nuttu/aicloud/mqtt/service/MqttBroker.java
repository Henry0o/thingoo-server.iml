package com.nuttu.aicloud.mqtt.service;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.UUID;

public class MqttBroker {
    private static int qos = 0; // 只有一次
    private static String broker = "tcp://120.76.249.195:1883";
    private static String userName = "admin";
    private static String passWord = "admin";
    private static String clientId = "clientTest";

    // broker为主机名，clientId为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，

    private static MqttClient connect(String clientId, String userName, String password) throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        MemoryPersistence persistence = new MemoryPersistence();
        // MQTT的连接设置
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        connOpts.setCleanSession(true);
        // 设置连接的用户名
        connOpts.setUserName(userName);
        // 设置连接的密码
        connOpts.setPassword(password.toCharArray());
        // 设置超时时间 单位为秒
        connOpts.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        connOpts.setKeepAliveInterval(20);
        // String[] uris = {"tcp://192.168.1.83:1883","tcp://192.168.1.87:1883"};
        // connOpts.setServerURIs(uris); //起到负载均衡和高可用的作用
        // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
        MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
        // 设置回调函数
        mqttClient.setCallback(new PushCallback());
        //连接服务器
        mqttClient.connect(connOpts);
        return mqttClient;
    }

    private static void pub(MqttClient sampleClient, String topic, String msg)
            throws MqttPersistenceException, MqttException {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        message.setRetained(false);
        sampleClient.publish(topic, message);
    }

    public static void publish(String topic, String msg) throws MqttException {
        if (clientId.equals("clientTest")) {
            clientId = UUID.randomUUID().toString();
        }
        MqttClient mqttClient = connect(clientId, userName, passWord);

        if (mqttClient != null) {
            System.out.println("开始发布");
            pub(mqttClient, topic, msg);
            System.out.println("pub-->topic=" + topic + ", msg=" + msg);
        }

        if (mqttClient != null) {
            mqttClient.disconnect();
        }
    }

}
