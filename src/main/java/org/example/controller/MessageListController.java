package org.example.controller;

import lombok.Data;
import org.example.dto.MessageWithSenderInfo;
import org.example.entity.Message;
import org.example.service.MessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageListController {

    @Autowired
    private MessageListService messageListService;

    @Data
    static class MessageRequest {
        private Integer groupId; // 使用 Integer 以便可以判断是否为 null
        private Integer uid;     // 使用 Integer 以便可以判断是否为 null
    }

    @PostMapping("/user")
    public List<MessageWithSenderInfo> getMessages(@RequestBody MessageRequest messageRequest) {
        if (messageRequest.getUid() != null && messageRequest.getUid() > 0) {
            // 如果 uid 有效，调用 getMessageList
            return messageListService.getMessageList(messageRequest.getUid(), messageRequest.getGroupId());
        } else if (messageRequest.getGroupId() != null) {
            // 如果只有 groupId，调用 getGroupMessageList
            return messageListService.getGroupMessageList(messageRequest.getGroupId());
        }
        throw new IllegalArgumentException("Either UID or Group ID must be provided.");
    }
}
