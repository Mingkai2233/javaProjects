package org.example.springbootheadlinepart.service;

import org.example.springbootheadlinepart.pojo.Headline;
import org.example.springbootheadlinepart.util.Result;

public interface HeadlineService {

    Result<Object> publish(Headline headline);

    Result<Object> findHeadlineByHid(Integer hid);

    Result<Object> update(Headline headline);

    Result<Object> removeByHid(Integer hid);
}
