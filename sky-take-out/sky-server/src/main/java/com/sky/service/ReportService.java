package com.sky.service;


import com.sky.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    Result<Object> turnoverStatistics(LocalDate begin, LocalDate end);

    Result<Object> userStatistics(LocalDate begin, LocalDate end);

    Result<Object> ordersStatistics(LocalDate begin, LocalDate end);

    Result<Object> top10(LocalDate begin, LocalDate end);

    void exportBusinessData(HttpServletResponse response);
}
