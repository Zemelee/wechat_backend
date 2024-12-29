package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("groups")
public class Group {
    @TableId  // 添加主键注解
    @TableField("group_id")
    private Integer groupId;  // 使用包装类型

    @TableField("group_name")
    private String groupName;

    @TableField("group_avatar")
    private String groupAvatar;

    @TableField("creator_id")
    private Integer creatorId;  // 使用包装类型

    @TableField("create_at")
    private Date createAt;

    private Integer multi;  // 使用包装类型
}
