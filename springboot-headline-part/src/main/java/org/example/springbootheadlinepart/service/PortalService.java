package org.example.springbootheadlinepart.service;

import org.example.springbootheadlinepart.pojo.PortalVo;
import org.example.springbootheadlinepart.pojo.Type;
import org.example.springbootheadlinepart.util.Result;

import java.util.List;
import java.util.Map;


public interface PortalService {
    Result<List<Type>> findAllTypes();

    Result<Map<String, Object>> findNewsPage(PortalVo portalVo);

    Result<Object> showHeadlineDetail(Integer hid);
}
