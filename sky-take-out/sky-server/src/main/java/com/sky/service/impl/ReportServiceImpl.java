package com.sky.service.impl;

import com.sky.mapper.OrderDetailMapper;
import com.sky.pojo.OrderCount;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.pojo.Top10Data;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    private final OrdersMapper ordersMapper;
    private final UserMapper userMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final WorkspaceService workspaceService;


    @Autowired
    public ReportServiceImpl(OrdersMapper ordersMapper,
                             UserMapper userMapper,
                             OrderDetailMapper orderDetailMapper, WorkspaceService workspaceService) {
        this.ordersMapper = ordersMapper;
        this.userMapper = userMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.workspaceService = workspaceService;
    }

    private List<LocalDate> getTimeList(LocalDate begin, LocalDate end) {
        List<LocalDate> times = new ArrayList<LocalDate>();
        LocalDate tmp = begin;
        while (tmp.isBefore(end)) {
            times.add(tmp);
            tmp = tmp.plusDays(1);
        }
        times.add(end);
        return times;
    }

    @Override
    public Result<Object> turnoverStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        TurnoverReportVO res = new TurnoverReportVO();
        // 获取时间区间内的日期
        List<LocalDate> times = getTimeList(begin, end);
        // 将日期拼接为指定格式
        res.setDateList(times.stream().map(LocalDate::toString).collect(Collectors.joining(",")));
        // TODO SQL优化，直接每天的统计结果
        // 查询订单在该时间段内的订单
        List<Orders> orders = ordersMapper.selectBatchByOrderTimeAndPayStatus(beginTime, endTime, Orders.PAID);
        Map<String, Double> map = new HashMap<>();
        for (LocalDate time : times) map.put(time.toString(), 0.0);
        for (Orders order : orders) {
            String time = order.getOrderTime().toLocalDate().toString();
            double amount = order.getAmount().doubleValue();
            amount += map.get(time);
            map.put(time, amount);
        }
        // 拼接金额数据
        StringJoiner sj = new StringJoiner(",");
        for (LocalDate time : times) sj.add(map.get(time.toString()).toString());
        res.setTurnoverList(sj.toString());

        return Result.success(res);
    }

    @Override
    public Result<Object> userStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        UserReportVO res = new UserReportVO();
        // 获取时间区间内的日期
        List<LocalDate> times = getTimeList(begin, end);
        // 将日期拼接为指定格式
        String detailList = times.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        // TODO SQL优化，二合一，并且直接返回数量
        // 查询在指定日期区间内创建的用户数据
        List<User> users = userMapper.selectBatchByCreateTime(beginTime, endTime);
        // 查询区间开始时间之前的存量用户
        Long totalUsersNumBase = userMapper.countBefore(beginTime);

        List<Long> newUsersNums = new ArrayList<>();
        List<Long> totalUsersNums = new ArrayList<>();
        Map<LocalDate, Long> map = new HashMap<>();
        for (int i = 0; i < times.size(); i++)
            map.put(times.get(i), 0L);
        // 统计区间内每天新增用户数量
        for (User user : users) {
            LocalDate curTime = user.getCreateTime().toLocalDate();
            map.put(curTime, map.get(curTime) + 1);
        }
        // 统计每天的总用户数量和新增量
        for (int i = 0; i < times.size(); i++) {
            newUsersNums.add(map.get(times.get(i)));
            if (i == 0) totalUsersNums.add(totalUsersNumBase + newUsersNums.get(i));
            else totalUsersNums.add(totalUsersNums.get(i - 1) + newUsersNums.get(i));
        }
        // 汇总结果
        String newUserList = newUsersNums.stream().map(Object::toString).collect(Collectors.joining(","));
        String totalUserList = totalUsersNums.stream().map(Object::toString).collect(Collectors.joining(","));
        res.setDateList(detailList);
        res.setNewUserList(newUserList);
        res.setTotalUserList(totalUserList);

        return Result.success(res);
    }

    @Override
    public Result<Object> ordersStatistics(LocalDate begin, LocalDate end) {
        String dateList;
        String orderCountList;
        String validOrderCountList;
        Double orderCompletetionRate;
        Integer totalOrderCount;
        Integer validOrderCount;
        // 获得日期列表
        List<LocalDate> times = getTimeList(begin, end);
        dateList = times.stream().map(LocalDate::toString).collect(Collectors.joining(","));
        // 查询不同状态的订单数量， 任意状态的数量和状态为5已完成的订单数量
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<OrderCount> orderCounts = ordersMapper.countByTimeAndStatus(beginTime, endTime, null);
        List<OrderCount>  validOrderCounts = ordersMapper.countByTimeAndStatus(beginTime, endTime, Orders.COMPLETED);
        // 将每天的订单数量整理为列表
        Map<LocalDate, Integer> map = new HashMap<>();
        for (LocalDate time : times) map.put(time, 0);
        for (OrderCount oc: orderCounts) {
            Integer newCount = map.get(oc.getTime())+oc.getCount();
            map.put(oc.getTime(), newCount);
        }
        List<Integer> counts = new ArrayList<>();
        for (LocalDate time : times) counts.add(map.get(time));
        // 将每天的有效订单数量整理为列表
        map.clear();
        for (LocalDate time : times) map.put(time, 0);
        for (OrderCount oc : validOrderCounts) {
            Integer newCount = map.get(oc.getTime())+oc.getCount();
            map.put(oc.getTime(), newCount);
        }
        List<Integer> validCounts = new ArrayList<>();
        for (LocalDate time : times) validCounts.add(map.get(time));

        // 拼接订单数量列表
        orderCountList = counts.stream().map(Object::toString).collect(Collectors.joining(","));
        validOrderCountList = validCounts.stream().map(Object::toString).collect(Collectors.joining(","));
        // 计算订单完成率
        totalOrderCount = counts.stream().reduce(Integer::sum).orElse(0);
        validOrderCount = validCounts.stream().reduce(Integer::sum).orElse(0);
        orderCompletetionRate = totalOrderCount == 0 ? 0.0 : (double) validOrderCount / totalOrderCount;

        OrderReportVO res = OrderReportVO.builder()
                .dateList(dateList)
                .orderCountList(orderCountList)
                .validOrderCountList(validOrderCountList)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletetionRate)
                .build();
        return Result.success(res);
    }

    @Override
    public Result<Object> top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        // 获取该时间段的已完成订单id列表
        List<Long> orderIds = ordersMapper.selectIdByTime(beginTime, endTime, Orders.COMPLETED);
        List<Top10Data> datas = orderDetailMapper.selectTop10(orderIds);

        String nameList = datas.stream().map(Top10Data::getName).collect(Collectors.joining(","));
        String numberList = datas.stream().map(Top10Data::getNumber).map(Object::toString).collect(Collectors.joining(","));

        SalesTop10ReportVO vo = SalesTop10ReportVO.builder().numberList(numberList).nameList(nameList).build();
        return Result.success(vo);
    }

    /**
     * 导出运营数据报表
     * @param response
     */
    public void exportBusinessData(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
            // TODO mysql优化
            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
