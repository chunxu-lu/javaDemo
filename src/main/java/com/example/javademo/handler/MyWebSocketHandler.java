package com.example.javademo.handler;

import com.example.javademo.service.RedisService;
import com.example.javademo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component // 确保这个处理器被Spring管理
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final RedisService redisService;
    private final JwtUtil jwtUtil; // 注入JwtUtil

    // 构造函数注入
    @Autowired
    public MyWebSocketHandler(RedisService redisService, JwtUtil jwtUtil) {
        this.redisService = redisService;
        this.jwtUtil = jwtUtil; // 初始化jwtUtil
    }

    // 静态集合存储所有活跃的WebSocket会话
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("新连接建立: " + session.getId());
        sessions.add(session); // 将新会话添加到集合

        // 加载历史消息并发送给新连接的用户
        List<String> messages = redisService.getMessageHistory();
        for (String msg : messages) {
            session.sendMessage(new TextMessage(msg));
        }

        String jwt = session.getUri().getQuery(); // 假设JWT通过URL查询参数传递
        String userName = "匿名用户";
        if (jwt != null && jwt.startsWith("jwt=")) {
            jwt = jwt.substring(4); // 去掉 "jwt=" 前缀
            userName = jwtUtil.getUsernameFromToken(jwt); // 使用JwtUtil获取用户名
            if (userName == null || userName.isEmpty()) {
                userName = "匿名用户";
            }
        }
        String joinMessage = userName + "加入了群聊！";
        // 广播加入群聊的消息给所有用户
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(joinMessage));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("message: " + message);
        System.out.println("收到消息: " + message.getPayload());
        JSONObject json = new JSONObject(message.getPayload());
        String msg = json.getString("user") + ": " + json.getString("message");
        // 广播消息给所有用户
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(msg));
            }
        }
        // 将新消息保存到Redis中
        redisService.saveMessage(msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接关闭: " + session.getId());
        sessions.remove(session); // 从集合中移除关闭的会话

        // 获取用户名
        String jwt = session.getUri().getQuery(); // 假设JWT通过URL查询参数传递
        String userName = "匿名用户";
        if (jwt != null && jwt.startsWith("jwt=")) {
            jwt = jwt.substring(4); // 去掉 "jwt=" 前缀
            userName = jwtUtil.getUsernameFromToken(jwt); // 使用JwtUtil获取用户名
            if (userName == null || userName.isEmpty()) {
                userName = "匿名用户";
            }
        }

        // 广播退出群聊的消息给所有用户
        String leaveMessage = userName + "离开了群聊！";
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(leaveMessage));
            }
        }
    }
}