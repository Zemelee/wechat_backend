package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user")
    List<User> getAllUser();

    @Select("select * from user where email = #{email} and password = #{password}")
    User Login(String email, String password);

    //判断是否已注册
    @Select("select count(*) from user where email = #{email}")
    int isRegister(String email);

    //检查验证码
    @Select("select code from temp where email = #{email}")
    String checkCode(String email);

    // 如果发送过，则更新验证码
    @Select("update temp set code = #{code} where email = #{email}")
    void updateCode(String email, String code);

    @Insert("insert into temp(email, code) values (#{email}, #{code})")
    void insertTemp(String email, String code);


    //根据uid查询用户
    @Select("select * from user where uid = #{uid}")
    User getUserByUid(int uid);

    //更新用户语句
    @Update("UPDATE user SET email = #{email}, password = #{password}, nickname = #{nickname}, avatar = #{avatar}, create_at = #{create_at}, address = #{address}, sign = #{sign} WHERE uid = #{uid}")
    int updateUser(User user);


    //修改密码
    @Update("update user set password = #{password} where uid = #{uid}")
    int updatePassword(String password, int uid);

    //查询密码
    @Select("select password from user where uid = #{uid}")
    String getPassword(int uid);

    //查询用户昵称
    @Select("SELECT nickname FROM user WHERE uid = #{uid}")
    String getNicknameByUid(Integer uid); // 通过 uid 查询 nickname
}