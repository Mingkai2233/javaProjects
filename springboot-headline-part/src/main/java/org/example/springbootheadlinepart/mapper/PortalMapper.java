package org.example.springbootheadlinepart.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springbootheadlinepart.pojo.PortalVo;
import org.example.springbootheadlinepart.pojo.Type;

import java.util.List;

@Mapper
public interface PortalMapper {
    List<Type> selectAll();
}
