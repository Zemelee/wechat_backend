package org.example.service;

import org.example.entity.Group;
import org.example.entity.User;
import org.example.mapper.GroupMapper;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;

    // 添加好友
    public String addFriend(int requestId, int receiveId) {
        // 判断目标用户是否存在
        User receiveUser = userMapper.getUserByUid(receiveId);
        if (receiveUser == null) {
            return "用户不存在";
        }

        Integer friendCount = groupMapper.getShipGid(requestId, receiveId);
        if (friendCount != null && friendCount > 0) {
            return "已经是好友";
        }

        try {
            Group newGroup = new Group();
            newGroup.setGroupName(String.valueOf(System.currentTimeMillis() / 1000)); // 默认群名为当前时间
            newGroup.setCreatorId(requestId); // 更新为正确的字段名
            newGroup.setCreateAt(new Date()); // 更新为正确的字段名
            newGroup.setMulti(0); // 私聊标识

            // 创建群聊并获取返回结果
            Integer result = groupMapper.createGroup(newGroup);
            if (result == 0) {
                return "添加失败"; // 处理创建失败的情况
            }

            int groupId = newGroup.getGroupId(); // 更新为正确的字段名
            System.out.println(result);
            System.out.println(groupId);

            User requestUser = userMapper.getUserByUid(requestId);
            groupMapper.createRelationship(groupId, requestId, requestUser.getNickname());
            groupMapper.createRelationship(groupId, receiveId, receiveUser.getNickname());

            return "添加成功";
        } catch (Exception e) {
            System.out.println("error: " + e);
            return "未知错误"; // 返回未知错误
        }
    }


    public String delFriend(int requestId, int receiveId) {
        User receiveUser = userMapper.getUserByUid(receiveId);
        if (receiveUser == null) return "用户不存在";
        Integer gid = groupMapper.getShipGid(requestId, receiveId);
        if (gid == null) return "已经不是好友";
        try {
            groupMapper.delRelationship(gid);
            groupMapper.delGroup(gid);
            return "删除成功";
        } catch (Exception e) {
            System.out.println("error:" + e);
            return "未知错误";
        }
    }

    // 获取好友列表
    public List<User> getFriendList(int uid) {
        List<User> friendList = new ArrayList<>();
        List<Integer> gids = groupMapper.getGidByUid(uid);// 获取用户加入的所有私聊群id
        for (Integer gid : gids) {  //通过群id获取所有好友id(遍历每个群的每个uid)
            List<Integer> uids = groupMapper.getUidByGid(gid);
            for (Integer uid1 : uids) {
                if (uid1 != uid) {
                    User user = userMapper.getUserByUid(uid1);
                    user.setPassword("****");
                    friendList.add(user);
                }
            }
        }
        return friendList;
    }

    // 新增多人群聊
    public String addGroup(String groupName, int creatorId, List<Integer> uids) {
        try {
            Group multiGroup = new Group();
            multiGroup.setGroupName(groupName); // 更新为正确的字段名
            multiGroup.setCreatorId(creatorId); // 更新为正确的字段名
            multiGroup.setCreateAt(new Date()); // 更新为正确的字段名
            multiGroup.setMulti(1); // 标识为多人群聊

            // 创建群聊并获取返回结果
            Integer res = groupMapper.createGroup(multiGroup);
            if (res == 0) {
                return "新建失败"; // 处理创建失败的情况
            }

            int gid = multiGroup.getGroupId(); // 更新为正确的字段名
            System.out.println("新建群聊的 gid: " + gid);

            // 将用户加入关系表
            for (Integer uid : uids) {
                String nickname = userMapper.getUserByUid(uid).getNickname();
                groupMapper.createRelationship(gid, uid, nickname);
            }

            return String.valueOf(gid); // 返回 gid
        } catch (Exception e) {
            System.out.println("error: " + e);
            return "未知错误"; // 返回未知错误
        }
    }


    //邀请加入群聊
    public String JoinGroup(int gid, List<Integer> uids) {
        try {
            for (Integer uid : uids) {
                User user = userMapper.getUserByUid(uid);
                if (user == null) {
                    return "用户不存在";
                }
                Integer count = groupMapper.isUserInGroup(gid, uid);
                if (count != null && count > 0) {
                    return "用户已在群聊中";
                }
            }
            for (Integer uid : uids) {
                User user = userMapper.getUserByUid(uid);
                groupMapper.createRelationship(gid, uid, user.getNickname());
            }
            return "邀请成功";
        } catch (Exception e) {
            System.out.println("error:" + e);
            return "未知错误";
        }
    }

    //踢人
    public void delGroupMember(int gid, int uid) {
        // 判断是否在群聊中
        Integer count = groupMapper.isUserInGroup(gid, uid);
        if (count == null || count == 0) {
            return;
        }
        try {
            groupMapper.delMember(gid, uid);
        } catch (Exception e) {
            System.out.println("error:" + e);
        }
    }
    // 解散群聊
    public Boolean delGroup(int gid) {
        try {
            groupMapper.delGroup(gid);
            groupMapper.delRelationship(gid);
            return true;
        } catch (Exception e) {
            System.out.println("error:" + e);
            return false;
        }
    }
    //获取群聊
    public List<Group> getGroupsByUid(int uid) {
        System.out.println("Fetching groups for UID: " + uid); // 添加日志

        // 第一步：获取用户的群组 ID
        List<Integer> groupIds = groupMapper.findGroupIdsByUserId(uid);
        if (groupIds.isEmpty()) {
            System.out.println("No group IDs found for UID: " + uid); // 如果没有群组 ID，打印日志
            return new ArrayList<>(); // 返回空列表
        }
        System.out.println("Group IDs found: " + groupIds);
        List<Group> groups = new ArrayList<>(); // 初始化 groups 列表
        // 第二步：根据群组 ID 获取群信息
        for (Integer groupId : groupIds) {
            System.out.println("Fetching group info for group ID: " + groupId);
            Group group = groupMapper.findGroupByIdAndMulti(groupId);// 假设你有一个方法根据 groupId 查询
            System.out.println("Group info: " + group);
                if (group != null) {
                groups.add(group);
            }
        }

        System.out.println("Groups fetched: " + groups); // 打印结果
        return groups;
    }
}