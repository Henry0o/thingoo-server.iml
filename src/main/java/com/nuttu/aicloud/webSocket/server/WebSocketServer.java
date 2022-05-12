package com.nuttu.aicloud.webSocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wing
 */
@ServerEndpoint(value = "/ws/{gatewayMac}")
@Component
public class WebSocketServer {
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private Session session;
    private String gatewayMac;
    @PostConstruct
    public void init(){
        System.out.println("WebSocket 加载中········");
    }

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
//    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    /**
     * 线程安全的set,存放每个客户端对应的session对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> sessionSet =new CopyOnWriteArraySet<WebSocketServer>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();

    /**
     * 连接建立成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "gatewayMac") String gatewayMac){
        this.session = session;
        this.gatewayMac = gatewayMac;
        sessionSet.add(this);
        sessionPool.put(gatewayMac,session);
        // int cnt = OnlineCount.incrementAndGet();
        log.info("有客户端加入连接，当前链接数为:{}",sessionSet.size());
    }

    /**
     * 关闭连接
     *
     */
    @OnClose
    public void onClose(){
        sessionSet.remove(this);
        sessionPool.remove(this.gatewayMac);
        //  int cnt = OnlineCount.decrementAndGet();
        log.info("有客户端关闭连接,当前连接数为:{}",sessionSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     *
     */
    @OnMessage
    public void onMessage(String message){
        log.info("来自客户端的消息:{}",message);
        sendOneMessage(this.gatewayMac,"收到消息，消息内容:"+message);
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onMessage(Session session,Throwable error){
        log.error("发生错误{},Session ID:{}",error,session.getId());
        error.printStackTrace();
    }
//
//    /**
//     * 发送消息 每次浏览器刷新，session会发生变化
//     * @param session
//     * @param message
//     */
//    public static void sendMessage(Session session,String message){
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            log.error("发送消息出错:{}",e.getMessage());
//            e.printStackTrace();
//        }
//    }

    /**
     * 群发消息
     * @param message
     * @throws IOException
     */
//    public static void broadCastInfo(String message) throws IOException{
//        for (Session s: sessionSet){
//            sendMessage(s,message);
//        }
//    }

//    public static void sendMessage(String message,String sessionId){
//        Session session = null;
//        for (Session s: sessionSet){
//            if (s.getId().equals(sessionId)){
//                session = s;
//                break;
//            }
//        }
//        if (session != null){
//            sendMessage(session,message);
//        }
//        else {
//            log.warn("没有找到你指定ID的会话:{}",sessionId);
//        }
//    }

    public static void sendAllMessage(String message){
        log.info("webSocket广播消息:{}",message);
        for (WebSocketServer webSocketServer:sessionSet){
            webSocketServer.session.getAsyncRemote().sendText(message);
        }
    }
    /**
     * 发送单点消息
     * @param gatewayMac
     * @param message
     */
    public static void sendOneMessage(@PathParam(value = "gatewayMac") String gatewayMac, String message){
        log.info("WebSocket单点消息:{}",message);
//        System.out.println(message);
        Session session = sessionPool.get(gatewayMac);
        if ((session != null)) {
            session.getAsyncRemote().sendText(message);
        }

    }

}
