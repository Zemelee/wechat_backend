package org.example.controller;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Group;
import org.example.entity.Message;
import org.example.service.MessageService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/ws/{userId}")
public class WebSocketController {

    private Session session;
    private String userId;
    private static final CopyOnWriteArraySet<WebSocketController> webSockets = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static MessageService messageService;
    private static UserService userService;

    @Autowired
    public void setService(MessageService messageService, UserService userService) {
        WebSocketController.messageService = messageService;
        WebSocketController.userService = userService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        webSockets.add(this);
        sessionPool.put(userId, session);
        System.out.println("Connected users: " + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        sessionPool.remove(this.userId);
        System.out.println("Disconnected userId: " + this.userId);
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            if (message.startsWith("ping")) {
                String[] parts = message.split("-");
                String uid = parts[1];// ping-uid
                Session session = sessionPool.get(uid);
                if (session != null && session.isOpen()) {
                    session.getBasicRemote().sendText("pong");
                }
                return;
            } else if (message.startsWith("create")) {
                String[] parts = message.split("-");
                if (parts.length > 1) {
                    String[] numberStrings = parts[1].split(",");
                    int[] userIds = new int[numberStrings.length];
                    for (int i = 0; i < numberStrings.length; i++) {
                        userIds[i] = Integer.parseInt(numberStrings[i]);
                    }
                    //对numbers中每个uid, 均发送消息refresh
                    for (int uid : userIds) {
                        sendOneMessage(String.valueOf(uid), "refresh");
                    }
                }
                return;
            }
            Message msg = objectMapper.readValue(message, Message.class);
            System.out.println("Received message: " + msg);
            // 撤回
            if (msg.getBack() == 1) {
                int Mid = msg.getMid();
                messageService.updateBackByMid(Mid);
            } else {
                messageService.saveMessage(msg);
            }
            handleMessageSend(msg, message);
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println("Error: " + error.getMessage());
    }

    private void handleMessageSend(Message msg, String msgText) {
        int groupId = msg.getGroupId();
        Group group = messageService.getGroupById(groupId);
        int Uid = msg.getUid();
        String nickname = messageService.getNicknameByUid(Uid);

        if (msg.getBack() == 1) {
            msgText = nickname + "撤回了一条消息";
        }
        if (group == null) {
            sendOneMessage(String.valueOf(groupId), msgText);
            return;
        }

        List<String> userIds = messageService.getUserIdsByGroupId(groupId);
        if (group.getMulti() == 1) { // Group chat
            int uid = msg.getUid(); //发送者id
            //根据uid获取用户昵称、头像
            nickname = userService.getUserByUid(uid).getNickname();
            String avatar = userService.getUserByUid(uid).getAvatar();
            JSONObject jsonObject = new JSONObject(msgText);
            jsonObject.append("nickname", nickname);
            jsonObject.append("avatar", avatar);
            String updatedMsgText = jsonObject.toString();
            System.out.println("Group members: " + userIds);
            for (String userId : userIds) {
                if (userId.equals(this.userId)) continue;
                sendOneMessage(userId, updatedMsgText);
            }
        }
    }

    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
            System.out.println("Send message to userId " + userId + ": " + message);
        } else {
            System.err.println("Session for userId " + userId + " is not open or does not exist.");
        }
    }
}
