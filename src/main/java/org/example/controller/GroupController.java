package org.example.controller;


import lombok.Data;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserMapper userMapper;

    //新加好友调用，插入群聊表和关系表
    // {request_id,receive_id}
    @GetMapping("/friend/add/{request_id}/{receive_id}")
    public Response addFriend(@PathVariable int request_id, @PathVariable int receive_id) {
        String addInfo = groupService.addFriend(request_id, receive_id);
        if (Objects.equals(addInfo, "添加成功")) {
            User receiveUser = userMapper.getUserByUid(receive_id);
            return new Response(receiveUser);
        }
        return new Response(1, addInfo);
    }

    @GetMapping("/friend/del/{request_id}/{receive_id}")
    public Response delFriend(@PathVariable int request_id, @PathVariable int receive_id) {
        String delInfo = groupService.delFriend(request_id, receive_id);
        if (Objects.equals(delInfo, "删除成功")) {
            return new Response(200, delInfo);
        }
        return new Response(1, delInfo);
    }

    @GetMapping("/friend/list/{uid}")
    public Response getFriendList(@PathVariable int uid) {
        return new Response(groupService.getFriendList(uid));
    }


    @Data
    static class GroupParam {
        private List<Integer> uids;
        private String group_name;
        private int creator_id;
    }

    @PostMapping("/group/add")
    public Response addGroup(@RequestBody GroupParam groupInfo) {
        String group_name = groupInfo.getGroup_name();
        int creator_id = groupInfo.getCreator_id();
        List<Integer> uids = groupInfo.getUids();
        if (uids.isEmpty()) {
            return new Response(1, "请至少选择一个好友");
        }
        String res = groupService.addGroup(group_name, creator_id, uids);
        // 如果res为数字字符串则表示成功
        if (res.matches("\\d+")) {
            return new Response(Integer.parseInt(res));
        }
        return new Response(1, res);
    }

    @PostMapping("/group/join/{gid}")
    public Response joinGroup(@RequestBody List<Integer> uids, @PathVariable int gid) {
        String res = groupService.JoinGroup(gid, uids);
        if (Objects.equals(res, "邀请成功")) {
            return new Response();
        }
        return new Response(1, res);
    }

    //踢人
    @PostMapping("/group/del_user/{gid}")
    public Response delGroupUser(@PathVariable int gid, @RequestBody List<Integer> uids) {
        try {
            for (Integer uid : uids) {
                groupService.delGroupMember(gid, uid);
            }
            return new Response();
        } catch (Exception e) {
            return new Response(1, "移除失败");
        }
    }

    @GetMapping("/group/disband/{gid}")
    public Response delGroup(@PathVariable int gid) {
        Boolean res = groupService.delGroup(gid);
        if (res) {
            return new Response();
        }
        return new Response(1, "解散群聊失败");
    }

    @GetMapping("/group/list/{uid}")
        public Response getGroupList(@PathVariable int uid) {
            return new Response(groupService.getGroupsByUid(uid));
        }
    }
