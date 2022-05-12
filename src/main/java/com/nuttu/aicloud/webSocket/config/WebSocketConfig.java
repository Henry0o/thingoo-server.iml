package com.nuttu.aicloud.webSocket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author wing
 */
@Configuration
public class WebSocketConfig {
    /**
     *给Spring容器注入ServerEndpointExporter对象
     * 检测所有带有@serverEndpoint注解的Bean并注册它们
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
//        System.out.println("注入ServerEndpointExporter");
        return new ServerEndpointExporter();
    }
}
