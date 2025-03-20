package com.example.javademo.handler;

import com.example.javademo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.json.JSONObject;

@Component // 确保这个处理器被Spring管理
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final RedisService redisService;

    // 构造函数注入
    @Autowired
    public MyWebSocketHandler(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("新连接建立: " + session.getId());
        String userName = redisService.getData("userName");
        if (userName != null && !userName.isEmpty()) {
            session.sendMessage(new TextMessage(userName + "加入了群聊！"));
        } else {
            session.sendMessage(new TextMessage("匿名用户加入了群聊！"));
        }
        // 示例：在连接建立时保存会话ID到Redis
//        redisService.saveData("session:" + session.getId(), "connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("message: " + message);
        System.out.println("收到消息: " + message.getPayload());
//        Object messageData = message.getPayload();
        JSONObject json = new JSONObject(message.getPayload());
        session.sendMessage(new TextMessage(json.getString("user") + ": " + json.getString("message")));
        // 示例：处理消息时与Redis交互
//        redisService.saveData("message:" + session.getId(), message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接关闭: " + session.getId());
        // 示例：在连接关闭时更新Redis中的状态
//        redisService.saveData("session:" + session.getId(), "disconnected");
    }
}