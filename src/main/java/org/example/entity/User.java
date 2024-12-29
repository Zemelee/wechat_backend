package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor // 有参构造
@NoArgsConstructor  // 无参构造
@TableName("user") //
public class User {
    @TableId  // 添加主键注解
    private Integer uid;  // 使用包装类型
//    private Cookie token;
    private String email;
    private String password;
    private String nickname;
    private String avatar;
    @TableField("create_at")
    private Date createAt;
    private String address;
    private String sign; // 个性签名
}
