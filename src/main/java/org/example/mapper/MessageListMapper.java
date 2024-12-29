package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.dto.MessageWithSenderInfo;
import org.example.entity.Message;

import java.util.List;

@Mapper
public interface MessageListMapper extends BaseMapper<Message> {

    @Select(
            "SELECT m.*, " +
                    "CASE " +
                    "   WHEN m.group_id = #{uid} AND m.uid = #{groupId} THEN u2.avatar " +  // 当条件匹配时，使用 groupId 查询用户的头像
                    "   ELSE u.avatar " +  // 否则，使用原本的用户头像
                    "END AS senderAvatar, " +
                    "CASE " +
                    "   WHEN m.group_id = #{uid} AND m.uid = #{groupId} THEN u2.nickname " +  // 当条件匹配时，使用 groupId 查询用户的昵称
                    "   ELSE u.nickname " +  // 否则，使用原本的用户昵称
                    "END AS senderNickname " +
                    "FROM messages m " +
                    "LEFT JOIN user u ON m.uid = u.uid " +  // 默认使用消息发送者的头像和昵称
                    "LEFT JOIN user u2 ON m.group_id = #{uid} AND m.uid = #{groupId} AND u2.uid = #{groupId} " +  // 当条件匹配时，用 groupId 作为 uid 查询用户信息
                    "WHERE (m.group_id = #{groupId} AND m.uid = #{uid}) " +
                    "OR (m.group_id = #{uid} AND m.uid = #{groupId})"
    )
    List<MessageWithSenderInfo> selectMessages(int groupId, int uid);



    @Select("SELECT m.*, " +
        "u.avatar AS senderAvatar, " +
        "u.nickname AS senderNickname " +
        "FROM messages m " +
        "LEFT JOIN relationships r ON m.group_id = r.group_id " +
        "LEFT JOIN user u ON m.uid = u.uid " +
        "WHERE m.group_id = #{groupId}")
List<MessageWithSenderInfo> selectGroupList(int groupId);
}
