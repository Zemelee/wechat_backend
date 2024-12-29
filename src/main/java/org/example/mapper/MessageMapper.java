package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.entity.Group;
import org.example.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    // BaseMapper 已经包含了插入、查询等基础方法
    @Insert("INSERT INTO messages (uid, group_id, content, send_at) VALUES (#{uid}, #{groupId}, #{content}, #{sendAt})")
    int saveMessage(Message msg);
    //根据group_id获取群信息
    @Select("SELECT * FROM groups WHERE group_id = #{groupId}")
    Group findGroupById(@Param("groupId") int groupId);
    //根据groupId返回所有uid
    @Select("SELECT uid FROM relationships WHERE group_id = #{groupId}")
    List<Integer> selectUidByGroupId(int groupId);
    //根据mid修改back
    @Update("UPDATE messages SET back = 1 WHERE mid = #{mid}")
    int updateBackByMid(Integer mid);
}
