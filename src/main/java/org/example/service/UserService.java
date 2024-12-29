package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //发送验证码
    public String sendCode(String email) {
        // 创建一个查询包装器,指定查询条件为 email
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User existingUser = userMapper.selectOne(queryWrapper);
        System.out.println(existingUser);
        if (existingUser != null) {
            return "用户已存在";
        }
        String res = SendEmailService.sendVerificationEmail(email);
        if (res.matches("\\d+")) {
            if (userMapper.checkCode(email) != null) {
                userMapper.updateCode(email, res);
            } else {
                userMapper.insertTemp(email, res);// 暂存 code 到数据库
            }
        }
        return res;
    }

    //检查验证码是否正确, 参数是用户传入的code
    public Boolean checkCode(String email, String code) {
        return Objects.equals(code, userMapper.checkCode(email));
    }

    //邮箱搜索用户
    public User getUserByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }
    //通过uid获取用户信息
    public User getUserByUid(int uid) {
        return userMapper.getUserByUid(uid);
    }

    //昵称模糊搜索多位用户
    public List<User> getUsersByNickname(String nickname) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("nickname", nickname);
        return userMapper.selectList(queryWrapper);
    }
    //更新用户信息
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }
    //更新密码
    public int updatePassword(int uid,String oldPassword,String newPassword) {
        String currentPassword = userMapper.getPassword(uid);

        // 检查旧密码是否匹配
        if (currentPassword != null && currentPassword.equals(oldPassword)) {
            // 更新新密码
            return userMapper.updatePassword(newPassword,uid);
        } else {
            // 旧密码不正确，可以抛出异常或返回特定值
            return -1; // -1 表示旧密码不正确
        }
    }
}
