package org.example.service;

import org.example.entity.Group;
import org.example.entity.Message;
import org.example.mapper.MessageMapper;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    // 保存消息到数据库
    public String saveMessage(Message message) {
        message.setSendAt(new Date()); // 设置发送时间
        System.out.println(message);
        int result = messageMapper.saveMessage(message);
        return result > 0 ? "成功" : "失败"; // 这里可以考虑使用枚举或常量来表示状态
    }

    public Group getGroupById(Integer groupId) {
        return messageMapper.findGroupById(groupId);
    }

    public List<String> getUserIdsByGroupId(Integer groupId) {
        List<Integer> userIds = messageMapper.selectUidByGroupId(groupId); // 获取 Integer 列表
        return userIds.stream()
                .map(String::valueOf) // 将 Integer 转换为 String
                .collect(Collectors.toList()); // 收集为 List<String>
    }
    //获取用户昵称
    public String getNicknameByUid(Integer uid) {
        return userMapper.getNicknameByUid(uid);
    }
    //修改back字段
    public boolean updateBackByMid(Integer mid) {
        return messageMapper.updateBackByMid(mid) > 0;
    }

}
