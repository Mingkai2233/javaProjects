package org.example.springbootheadlinepart.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.springbootheadlinepart.pojo.Headline;
import org.example.springbootheadlinepart.pojo.PortalVo;

import java.util.List;
import java.util.Map;

public interface HeadlineMapper {
    List<Headline> selectByTitleAndType(@Param("keyword") String keyword, @Param("type") Integer type);
    Map<String, String> selectDetailMap(@Param("hid")Integer hid);
    Integer selectVersionById(@Param("hid") Integer hid);
    // 乐观锁实现
    Integer updatePageViewsById(@Param("hid") Integer hid, @Param("version") Integer version);

    Integer insertOne(Headline headline);
    Headline selectOneByID(@Param("hid") Integer hid);

    Integer updateByID(Headline headline);

    Integer deleteByID(@Param("hid") Integer hid);
}
