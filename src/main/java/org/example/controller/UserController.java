package org.example.controller;


import lombok.Data;
import org.example.config.JwtUtil;
import org.example.entity.Response;
import org.example.entity.Temp;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@RestController
@Controller
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public List<User> getAllUser() {
        return userMapper.selectList(null);
    }

    // 发送验证码的接口
    @PostMapping("/code/send/{email}")
    public Response sendVerificationCode(@PathVariable String email) {
        Response response = new Response();
        String result = userService.sendCode(email);
        // 如果返回的结果是数字字符串, 说明发送验证码成功
        if (result.matches("\\d+")) {
            return response;
        }
        // 用户已存在 发送失败
        response.setMsg(result);
        response.setCode(1);
        return response;
    }

    // 检查验证码是否正确
    @PostMapping("/code/check")
    public Response checkVerificationCode(@RequestBody Temp temp) {
        String email = temp.getEmail();
        String code = temp.getCode();
        boolean result = userService.checkCode(email, code);
        if (!result) {
            return new Response(1, "验证码错误");
        }
        return new Response();
    }

    @PostMapping("/user/register")
    public Response userRegister(@RequestBody User user) {
        if (userMapper.insert(user) > 0) {
            return new Response(200, "注册成功");
        }
        return new Response(1, "注册失败");
    }


    @PostMapping("/user/login")
    public Response login(@RequestBody User requestUser) throws Exception {
        String email = requestUser.getEmail();
        String password = requestUser.getPassword();
        User user = userMapper.Login(email, password);
        Response response = new Response();
        if (user != null) {
            user.setPassword("****");
            response.setData(user);
             response.setMsg("登录成功");
            String token = JwtUtil.createToken(email);
//            Cookie cookie = new Cookie("token", token);
            return response;
        }
        response.setCode(1);
        response.setMsg("用户名或密码错误");
        return response;
    }

    @GetMapping("/user/search")
    public Response getUserByUid(@RequestParam String keyword, @RequestParam String type) {
        Response response = new Response();
        if (type.equals("email")) {
            User user = userService.getUserByEmail(keyword);
            if (user != null) {
                user.setPassword("****");
                List<User> userList = new ArrayList<>();
                userList.add(user);
                response.setData(userList); //统一返回格式
                response.setMsg("获取成功");
                return response;
            }
        } else if (type.equals("nickname")) {
            List<User> userList = userService.getUsersByNickname(keyword);
            if (!userList.isEmpty()) {
                for (User user : userList) {
                    user.setPassword("****");
                }
                response.setData(userList);
                response.setMsg("获取成功");
                return response;
            }
        }
        response.setCode(1);
        response.setMsg("用户不存在");
        return response;
    }
    //更改个人信息
    @PostMapping("/user/update")
    public Response userUpdate(@RequestBody User user) {
        if (userService.updateUser(user) > 0) {
            return new Response(200, "修改成功");
        }
        return new Response(1, "修改失败");
    }
    @Data
    static class PsdInfo {
        int uid;
        String oldPassword;
        String newPassword;
    }
    //修改密码
    @PostMapping("/user/update/password")
    public Response updatePassword(@RequestBody PsdInfo psdInfo) {
        int result =userService.updatePassword(psdInfo.getUid(),psdInfo.getOldPassword(),psdInfo.getNewPassword());
        if( result== -1){
            return new Response(1, "旧密码不正确");
        } else if (result == 0) {
            return new Response(1, "修改失败");
        }
        return new Response(200, "修改成功");
    }

}
