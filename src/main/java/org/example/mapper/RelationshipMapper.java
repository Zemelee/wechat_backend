package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.Relationship;

import java.util.List;

public interface RelationshipMapper extends BaseMapper<Relationship> {
    @Select("SELECT group_id FROM relationships WHERE uid = #{uid}")
    List<Integer> selectGroupIdByUid(int uid);

}
