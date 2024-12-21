package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Slf4j
@Api(tags="管理端数据统计接口")
@RequestMapping("/admin/report")
@RestController
public class ReportController {
    private final ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/test")
    public Result<String> test(){
        return Result.success("test");
    }

    @ApiOperation("营业额统计")
    @GetMapping("/turnoverStatistics")
    public Result<Object> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")@Param("begin") LocalDate begin,
                                             @DateTimeFormat(pattern = "yyyy-MM-dd")@Param("end") LocalDate end) {
        log.info("营业额统计{}-{}", begin, end);
        return reportService.turnoverStatistics(begin, end);
    }

    @ApiOperation("用户数据统计")
    @GetMapping("/userStatistics")
    public Result<Object> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")@Param("begin") LocalDate begin,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd")@Param("end") LocalDate end) {
        log.info("用户统计{}-{}", begin, end);
        return reportService.userStatistics(begin, end);
    }


    @ApiOperation("订单数据统计")
    @GetMapping("/ordersStatistics")
    public Result<Object> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")@Param("begin") LocalDate begin,
                                           @DateTimeFormat(pattern = "yyyy-MM-dd")@Param("end") LocalDate end) {
        log.info("订单统计");
        return reportService.ordersStatistics(begin, end);
    }

    @ApiOperation("销量排名前十的菜品")
    @GetMapping("/top10")
    public Result<Object> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("查询销量排名前十的菜品");
        return reportService.top10(begin,end);
    }

    @ApiOperation("导出运营数据")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        log.info("导出数据");
        reportService.exportBusinessData(response);
    }
}
