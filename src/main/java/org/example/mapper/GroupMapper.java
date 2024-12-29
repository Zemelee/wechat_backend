package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.entity.Group;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {

    // 新建群聊（加好友也是此操作）
    @Insert("INSERT INTO groups(group_name, creator_id, create_at, multi) " +
            "VALUES (#{group.groupName}, #{group.creatorId}, #{group.createAt}, #{group.multi})")
    @Options(useGeneratedKeys = true, keyProperty = "group.groupId") // 返回主键
    Integer createGroup(@Param("group") Group group);

    // 根据好友id查询群id（判断是否已经是好友）
    @Select("SELECT group_id FROM relationships WHERE group_id IN (" +
            "SELECT group_id FROM relationships WHERE uid IN (#{uid1}, #{uid2}) " +
            "GROUP BY group_id) GROUP BY group_id " +
            "HAVING COUNT(DISTINCT uid) = 2 AND SUM(uid = #{uid1}) > 0 AND SUM(uid = #{uid2}) > 0")
    Integer getShipGid(@Param("uid1") int uid1, @Param("uid2") int uid2);

    // 创建关系
    @Insert("INSERT INTO relationships(group_id, uid, nickname) " +
            "VALUES (#{groupId}, #{uid}, #{nickname})")
    void createRelationship(int groupId, int uid, String nickname);

    // 根据gid解散群聊，与delGroup搭配使用
    @Delete("DELETE FROM relationships WHERE group_id = #{gid}")
    void delRelationship(int gid);

    // 删除群
    @Delete("DELETE FROM groups WHERE group_id = #{gid}")
    void delGroup(int gid);

    // 获取私聊列表id
    @Select("SELECT group_id FROM relationships WHERE group_id IN (" +
            "SELECT group_id FROM relationships WHERE uid = #{uid}) GROUP BY group_id " +
            "HAVING COUNT(*) = 2")
    List<Integer> getGidByUid(int uid);

    // 根据gid获取好友id
    @Select("SELECT uid FROM relationships WHERE group_id = #{gid}")
    List<Integer> getUidByGid(int gid);

    // 根据gid查找uid是否在群里
    @Select("SELECT uid FROM relationships WHERE group_id = #{gid} AND uid = #{uid}")
    Integer isUserInGroup(@Param("gid") int gid, @Param("uid") int uid);

    // 踢人
    @Delete("DELETE FROM relationships WHERE group_id = #{gid} AND uid = #{uid}")
    void delMember(int gid, int uid);

    // 查询相关的群
    // 根据用户 ID 获取群组 ID
    @Select("SELECT group_id FROM relationships WHERE uid = #{uid}")
    List<Integer> findGroupIdsByUserId(@Param("uid") int uid);

    // 根据群组 ID 列表获取群信息
    // 根据 group_id 和 multi 获取群信息
    @Select("SELECT * FROM groups WHERE group_id = #{groupId} AND multi = 1")
    Group findGroupByIdAndMulti(@Param("groupId") int groupId);
}
