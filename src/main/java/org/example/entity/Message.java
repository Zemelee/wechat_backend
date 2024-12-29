package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // 确保添加这个导入
import lombok.Data;

import java.util.Date;

@Data
@TableName("messages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    @TableId
    private Integer mid; // 主键，使用包装类型

    private Integer uid; // 用户 ID，使用包装类型

    @TableField("group_id") // 显示映射数据库字段名
    private Integer groupId; // 群组 ID，使用包装类型

    private String content; // 消息内容

    @TableField("send_at") // 显示映射数据库字段名
    private Date sendAt; // 发送时间
    private Integer back;  // 撤回消息判断

}
