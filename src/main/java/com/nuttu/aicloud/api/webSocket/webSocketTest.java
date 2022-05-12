package com.nuttu.aicloud.api.webSocket;

import com.nuttu.aicloud.webSocket.server.WebSocketServer;
import org.springframework.web.bind.annotation.*;

/**
 * @author wing
 */
@RestController
@RequestMapping(value = "/webSocket")
public class webSocketTest {
    @GetMapping("/sendAllWebSocket")
    public String test() {
        String text="你们好！这是websocket群体发送！";
        WebSocketServer.sendAllMessage(text);
        return text;
    }

    @GetMapping("/sendOneWebSocket/{uuid}")
    public String sendOneWebSocket(@PathVariable("uuid") String uuid,
                                   @RequestParam String message) {
//        String text=uuid+" 你好！ 这是websocket单人发送！";
        WebSocketServer.sendOneMessage(uuid,message);
        return "发送消息成功:"+ message;
    }
}
