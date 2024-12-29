package org.example.mapper;

import org.example.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageSendMapper extends BaseMapper<Message> {
    // BaseMapper 已经包含了插入、查询等基础方法
}

