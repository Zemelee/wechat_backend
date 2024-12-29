package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("relationships")
public class Relationship {
    @TableId  // 添加主键注解
    private Integer id;  // 使用包装类型

    private Integer group_id;  // 使用包装类型

    private Integer uid;  // 使用包装类型

    private String nickname;

    private String avatar;
}
