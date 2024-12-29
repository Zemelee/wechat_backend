package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.dto.MessageWithSenderInfo;
import org.example.entity.Message;
import org.example.mapper.MessageListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageListService {

    @Autowired
    private MessageListMapper messageListMapper;

    public List<MessageWithSenderInfo> getMessageList(int uid, int gid) {
        // 使用自定义的 SQL 查询方法，通过 MessageListMapper 获取带有发送者信息的消息列表
        return messageListMapper.selectMessages(uid, gid);
    }


    public List<MessageWithSenderInfo> getGroupMessageList(int gid) {
        // 直接调用 selectGroupList 方法
        return messageListMapper.selectGroupList(gid);
    }
}
