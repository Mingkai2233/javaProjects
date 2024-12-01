package org.example.springbootheadlinepart.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootheadlinepart.mapper.HeadlineMapper;
import org.example.springbootheadlinepart.mapper.PortalMapper;
import org.example.springbootheadlinepart.pojo.Headline;
import org.example.springbootheadlinepart.pojo.PortalVo;
import org.example.springbootheadlinepart.pojo.Type;
import org.example.springbootheadlinepart.service.PortalService;
import org.example.springbootheadlinepart.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class PortalServiceImpl implements PortalService {
    @Autowired
    private PortalMapper portalMapper;
    @Autowired
    private HeadlineMapper headlineMapper;

    @Override
    public Result<List<Type>> findAllTypes() {
        List<Type> types = portalMapper.selectAll();
        return Result.ok(types);
    }

    @Override
    public Result<Map<String, Object>> findNewsPage(PortalVo portalVo) {
        PageHelper.startPage(portalVo.getPageNum(), portalVo.getPageSize());
        List<Headline> list = headlineMapper.selectByTitleAndType(portalVo.getKeyWords(), portalVo.getType());
        PageInfo<Headline> pageInfo = new PageInfo<>(list);
        Map<String, Object> myPageInfo = new HashMap<>();
        myPageInfo.put("pageData", list);
        myPageInfo.put("pageNum", pageInfo.getPageNum());
        myPageInfo.put("pageSize", pageInfo.getSize());
        myPageInfo.put("totalPage", pageInfo.getPages());
        myPageInfo.put("totalSize", pageInfo.getTotal());
        Map<String, Object> data = new HashMap<>();
        data.put("pageInfo", myPageInfo);
        return Result.ok(data);
    }

    @Override
    public Result<Object> showHeadlineDetail(Integer hid) {
        Map<String, String> detailMap;
        Map<String, Object> result = new HashMap<>();
        Integer version;
        Integer rows;
        while (true){
            version = headlineMapper.selectVersionById(hid);
            detailMap = headlineMapper.selectDetailMap(hid);
            rows = headlineMapper.updatePageViewsById(hid, version);
            if (rows > 0) {
                log.debug("更新成功，当前浏览量为{}", detailMap.get("pageViews"));
                break;// 更新成功

            }
            log.debug("更新浏览量发生冲突！");
        }
        result.put("headline", detailMap);
        return Result.ok(result);
    }
}


