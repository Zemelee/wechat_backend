package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageWithSenderInfo {

    private Integer mid; // 消息主键
    private Integer uid; // 用户 ID
    private Integer groupId; // 群组 ID
    private String content; // 消息内容
    private Date sendAt; // 发送时间
    private Integer back; // 撤回消息判断

    // 新增字段：扩展信息
    private String senderAvatar; // 发送者头像
    private String senderNickname; // 发送者昵称
}
