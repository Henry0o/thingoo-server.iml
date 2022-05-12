package com.nuttu.aicloud.mqtt.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import com.nuttu.aicloud.model.gateway.Gateway;
import com.nuttu.aicloud.mqtt.model.StatusMessage;
import com.nuttu.aicloud.mqtt.service.MqttBroker;
import com.nuttu.aicloud.mqtt.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;


@IntegrationComponentScan
@EnableAutoConfiguration
@Component
@ComponentScan(basePackages = {"com.nuttu.aicloud"})
@Configuration
public class MqttConfig{

    private static Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    @Autowired
    private MqttService statusService;

    @Value("${user:admin}")
    private String user;

    @Value("${password:admin}")
    private String password;

    @Value("${clientId:clientTest}")
    private String clientId;

    @Value("${host:120.76.249.195}")
    private  String host;

    @Value("${port:1883}")
    private  int port;

    @Value("${url:tcp://120.76.249.195:1883}")
    private  String url;

    @Value("${waitTime:5}")
    private int waitTime;

    @Value("${qos:0}")
    private int qos;

    @Value("${topic:/gw/#,/dev/#}")
    private String topic;

    @Value("${numMessages:1000}")
    private long numMessages = 0;

    /**
     * MessageChannel method creates a new mqttInputChannel
     * to connect to the Broker with a single threaded connection.
     * Downstream components are connected via Direct Channel.
     *
     * @return   DirectChannel single threaded connection
     */

    /**
     * 配置Inbound入站，消费者订阅的消息通道
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT连接器选项

    @Bean
    public MqttConnectOptions getReceiverMqttConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        // 设置连接的用户名
        if(!user.trim().equals("")){
            options.setUserName(user);
        }
        // 设置连接的密码
        options.setPassword(password.toCharArray());
        // 设置连接的地址
        options.setServerURIs(StringUtils.split(url, ","));    //这里采用url方式配置
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(waitTime);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        // 但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        return options;
    }
     */

    /**
     * MqttPahoClientFactory method establishes the URL of the server along with the host and
     * port, the username, and the password for connecting to the determined broker.
     * Generates the Last Will and Testament for the publisher clientId
     * for a lost connection situation. SSL connection to the broker is possible with
     * the correct keyStore and trustStore provided.
     *
     * @return   factory with given variables
     */


    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        //java.util.Properties sslClientProps = new java.util.Properties();
        // TODO: Provide better logic in case user uses something besides the default MQTT ports
        if (port == 1883){
            factory.setServerURIs("tcp://" + host + ":" + port);
        } else{
            factory.setServerURIs("ssl://" + host + ":" + port);
        }

//        factory.setUserName(user);
//        factory.setPassword(password);
//        factory.setSslProperties(sslClientProps);
//        factory.setWill(new DefaultMqttPahoClientFactory.Will(topic, "I have died...".getBytes(), qos, true ));
        return factory;
    }

    /**
     * MessageProducer generates a clientID for the subscriber by using a randomUUID
     * for creating a connection to a broker. Creates a new
     * connection adapter to a broker by setting the clientID, MqttClientFactory,
     * topic given to subscribe to, Completion Timeout, Converter,
     * Qos, and the Output Channel for numMessages to go to.
     *
     * @return   adapter for the mqttInbound connection
     */
    @Bean
    public MessageProducer mqttInbound() {
        if (clientId.equals("clientTest")){
            //生成唯一UUID
            clientId = UUID.randomUUID().toString();

        }

        // 初始化入站通道适配器，使用的是Eclipse Paho MQTT客户端库
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), StringUtils.split(topic, ","));

        // 设置连接超时时长(默认100000毫秒)
        adapter.setCompletionTimeout(100000);
        // 配置默认Paho消息转换器(qos=0, retain=false, charset=UTF-8)
        adapter.setConverter(new DefaultPahoMessageConverter());
        // 设置服务质量
        // 0 最多一次，数据可能丢失;
        // 1 至少一次，数据可能重复;
        // 2 只有一次，有且只有一次;最耗性能
        adapter.setQos(qos);

        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * Message handler calculates the amount of messages that have come in
     * from the subscribed topic and posts the result.
     *
     * @return   message is the numMessages amount
     */

    /**
     * 配置Inbound入站，消费者的消息处理器
     * 1. 使用@ServiceActivator注解，表明所修饰的方法用于消息处理
     * 2. 使用inputChannel值，表明从指定通道中取值
     * 3. 利用函数式编程的思路，解耦MessageHandler的业务逻辑
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        final int GW_LEN = 4;
        final int UUID_LEN = 32;
        final int TOPIC_GW_BASE_LEN = GW_LEN + UUID_LEN + 7;
        return message -> {
            //if (count % 10 == 0) {
             //   System.out.println(String.format("Received %d num Messages from " + topic + ":" + message.getHeaders() + " topic.", count));
           // }

            //count++;
            //logger.info(message.getPayload().toString());

            String mqttTopic = (String) message.getHeaders().get("mqtt_topic");
            logger.info( message.getHeaders().toString());
            logger.info( message.toString());
            logger.info("mqttTopic:" + mqttTopic);
            if (mqttTopic.startsWith(PrefixAndSuffix.PRE_GW.getValue())) {
                logger.info("/gw/ process");
                if (mqttTopic.length() >= TOPIC_GW_BASE_LEN) {  //
                    String gatewayMac = mqttTopic.substring(GW_LEN, GW_LEN+UUID_LEN).toUpperCase();
                    gatewayMac = gatewayMac.toUpperCase();
                    Object messagePayload = message.getPayload();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.registerModule(new JodaModule());
                    logger.info("gatewayMac:" + gatewayMac);
                    logger.info("messagePayload:" + messagePayload);

                    try {
                        if (mqttTopic.endsWith(PrefixAndSuffix.SUF_STATUS.getValue())) {
                            List<StatusMessage> events = mapper.readValue(messagePayload.toString(), new TypeReference<List<StatusMessage>>() {
                            });
//                            StatusMessage events = mapper.readValue(messagePayload.toString(),StatusMessage.class);
                            logger.info("events:" + events);

                            statusService.handleGWStatus(gatewayMac, events);
                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_REGISTER.getValue())) {
                            logger.info("gateway register request: " + new String(message.getPayload().toString().getBytes(), StandardCharsets.UTF_8));
                            Gateway gateway = null;
                            try{
                                gateway = mapper.readValue(messagePayload.toString(), new TypeReference<Gateway>() {});
                            }catch (Exception e){
                                try {
                                    MqttBroker.publish("/ai/" + gatewayMac + "/reg_resp","JSON_ERR0R");
                                } catch (MqttException ex) {
                                    ex.printStackTrace();
                                }
                                e.printStackTrace();
                            }

                            logger.info("gateway register request: " + gateway);

                            statusService.handleGWRegister(gatewayMac, gateway);
                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_VER_QUERY.getValue())){
                            // TODO:版本查询回复消息处理
                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_SET_FPS.getValue())){

                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_SET_LOCATION.getValue())){

                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_EXPCAP_QUERY.getValue())){

                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_EXPPARAM_SET.getValue())){

                        } else if (mqttTopic.endsWith(PrefixAndSuffix.SUF_OPTEXP_SET.getValue())){

                        }


                    } catch (IOException e) {
                        //logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    logger.info("short topic received!");
                }
            } else if (mqttTopic.startsWith(PrefixAndSuffix.PRE_DEV.getValue())) {
                logger.info("/dev/ process");
            }
        };
    }


    /**
     * Creates a new instance of the PropertySourcesPlaceholderConfigurer
     * to pass property sources from the application.properties file.
     *
     * @return   PropertySourcesPlaceholderConfigurer new instance
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
