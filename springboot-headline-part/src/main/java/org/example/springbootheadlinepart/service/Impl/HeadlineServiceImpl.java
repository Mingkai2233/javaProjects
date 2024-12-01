package org.example.springbootheadlinepart.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootheadlinepart.mapper.HeadlineMapper;
import org.example.springbootheadlinepart.pojo.Headline;
import org.example.springbootheadlinepart.service.HeadlineService;
import org.example.springbootheadlinepart.util.Result;
import org.example.springbootheadlinepart.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class HeadlineServiceImpl implements HeadlineService {
    @Autowired
    HeadlineMapper headlineMapper;

    @Override
    public Result<Object> publish(Headline headline) {
        int rows = headlineMapper.insertOne(headline);
        log.debug("插入一条headline, hid为{}",headline.getHid());
        return Result.ok(null);
    }

    @Override
    public Result<Object> findHeadlineByHid(Integer hid) {
        Headline  headline = headlineMapper.selectOneByID(hid);
        Map<String, Object> partOfHeadline = new HashMap<>();
        partOfHeadline.put("hid", headline.getHid());
        partOfHeadline.put("title", headline.getTitle());
        partOfHeadline.put("article", headline.getArticle());
        partOfHeadline.put("type", headline.getType());
        Map<String, Object> data = new HashMap<>();
        data.put("headline", partOfHeadline);
        return Result.ok(data);
    }

    @Override
    public Result<Object> update(Headline headline) {
        int rows = headlineMapper.updateByID(headline);
        if (rows > 0) return Result.ok(null);
        log.error("更新失败，hid为{}", headline.getHid());
        return Result.build(null, ResultCodeEnum.COMMON_ERROR);
    }

    @Override
    public Result<Object> removeByHid(Integer hid) {
        int rows = headlineMapper.deleteByID(hid);
        if (rows > 0) return Result.ok(null);
        log.error("删除失败，hid为{}", hid);
        return Result.build(null, ResultCodeEnum.COMMON_ERROR);
    }
}
