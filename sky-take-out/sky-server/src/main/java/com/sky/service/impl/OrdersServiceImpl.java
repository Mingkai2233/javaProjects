package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrdersMapper ordersMapper;
    private final AddressBookMapper addressBookMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserMapper userMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final WeChatPayUtil weChatPayUtil;
    private final WebSocketServer webSocketServer;
    private Orders orders;
    // 地图定位功能
    @Value("${sky.shop.address}")
    private String shopAddress;
    @Value("${sky.baidu.ak}")
    private String ak;
    public OrdersServiceImpl(OrdersMapper ordersMapper,
                             AddressBookMapper addressBookMapper,
                             ShoppingCartMapper shoppingCartMapper,
                             UserMapper userMapper,
                             OrderDetailMapper orderDetailMapper,
                             WeChatPayUtil weChatPayUtil,
                             WebSocketServer webSocketServer) {
        this.weChatPayUtil = weChatPayUtil;
        this.orderDetailMapper = orderDetailMapper;
        this.userMapper = userMapper;
        this.ordersMapper = ordersMapper;
        this.addressBookMapper = addressBookMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.webSocketServer = webSocketServer;
    }


    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public Result<OrderSubmitVO> submit(OrdersSubmitDTO ordersSubmitDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        Long userId = BaseContext.getCurrentId();
        // 1处理业务异常
        // 判断传入的地址是否有效
        AddressBook addressBook = addressBookMapper.getById(orders.getAddressBookId());
        if (addressBook == null) throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        // 判断购物车是否为空
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectBatchByUserId(userId);
        if (shoppingCarts == null || shoppingCarts.isEmpty())
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        // 2填充数据 并在order表插入一条数据
        orders.setNumber(String.valueOf(System.currentTimeMillis())+"-"+userId);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        User user = userMapper.selectOneById(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        ordersMapper.insertOne(orders);
        // 3向order_detail插入多条数据
        // 组装数据
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for (ShoppingCart cart: shoppingCarts){
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setId(null);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        }
        // 插入数据
        orderDetailMapper.insertBatch(orderDetails);
        // 4清空购物车
        shoppingCartMapper.deleteAll(userId);

        // 准备返回数据
        OrderSubmitVO res = OrderSubmitVO.builder().
                id(orders.getId()).
                orderNumber(orders.getNumber()).
                orderAmount(orders.getAmount()).
                orderTime(orders.getOrderTime()).build();
        this.orders = orders;
        return Result.success(res);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.selectOneById(userId);

//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        return vo;

        // 模拟支付
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        Integer orderPaidStatus = Orders.PAID;//支付状态，已支付
        Integer orderStatus = Orders.TO_BE_CONFIRMED;  //订单状态，待接单
        LocalDateTime check_out_time = LocalDateTime.now();//更新支付时间
        ordersMapper.updateStatus(orderStatus, orderPaidStatus, check_out_time, this.orders.getId());
        // 给商家发送来单提醒
        Map<String, String> data = new HashMap<>();
        data.put("type", "1");
        data.put("orderId", ordersPaymentDTO.getOrderNumber());
        data.put("content", "订单号"+ordersPaymentDTO.getOrderNumber());
        String json = JSON.toJSONString(data);
        webSocketServer.sendToAllClient(json);
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

    /**
     * 查询历史订单
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public Result<Object> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        Long userId = BaseContext.getCurrentId();
        PageResult res = new PageResult();
        Integer status = ordersPageQueryDTO.getStatus();
        // 先查询order表
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOs = ordersMapper.selectBatchByUserIdAndStatus(userId,status);
        res.setTotal(orderVOs.getTotal());
        // 查询order_detail表
        for (OrderVO ordervo : orderVOs){
            ordervo.setOrderDetailList(orderDetailMapper.selectBatchByOrderId(ordervo.getId()));
        }
        res.setRecords(orderVOs);
        return Result.success(res);
    }

    /**
     * 获取指定订单的信息
     * @param id
     * @return
     */
    @Override
    public Result<Object> getOrder(Long id) {
        OrderVO ordervo = ordersMapper.selectOneByOrderId(id);
        ordervo.setOrderDetailList(orderDetailMapper.selectBatchByOrderId(id));
        return Result.success(ordervo);
    }

    /**
     * 取消指定订单
     * @param id 订单id
     * @return
     */
    @Override
    public Result<Object> cancel(Long id) {
        Orders orderDB = ordersMapper.selectOneByOrderId(id);
        // 订单不存在
        if (orderDB == null) throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        // 订单处于不可取消状态
        if (orderDB.getStatus() > 2) throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        // 订单可取消
        Orders order = Orders.builder().
                id(orderDB.getId()).
                cancelTime(LocalDateTime.now()).
                status(Orders.CANCELLED).
                payStatus(Orders.REFUND).
                cancelReason("用户取消订单").
                build();
        ordersMapper.update(order);
        return Result.success("取消成功");
    }

    /**
     * 再来一单, 将历史订单中的菜品重新加入购物车
     * @param id 订单id
     * @return
     */
    @Override
    public Result<Object> repetition(Long id) {
        // 查询order_detail表
        List<OrderDetail> orderDetails = orderDetailMapper.selectBatchByOrderId(id);
        // TODO 筛除历史订单中已经停售的菜品和套餐
        // 将OrderDetail转换为ShoppingCart
        List<ShoppingCart> shoppingCarts = orderDetails.stream().map(od -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(od, cart);
            cart.setId(null);
            cart.setUserId(BaseContext.getCurrentId());
            cart.setCreateTime(LocalDateTime.now());
            return cart;
        }).collect(Collectors.toList());
        // 插入shopping_cart
        shoppingCartMapper.insertBatch(shoppingCarts);
        return Result.success();
    }

    /**
     * 根据条件 查询数据库，获得订单信息
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public Result<Object> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult res = new PageResult();
        // 查询订单信息
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<OrderVO> orderVOs = ordersMapper.selectByCondition(ordersPageQueryDTO);
        res.setTotal(orderVOs.getTotal());
        // 查询订单详细信息，然后拼接菜品名字和数量
        // TODO 改为多表查询，一次性拿取全部数据
        for (OrderVO order:orderVOs){
            order.setOrderDetailList(orderDetailMapper.selectBatchByOrderId(order.getId()));
            String dishes = "";
            for (OrderDetail detail : order.getOrderDetailList()){
                dishes += detail.getName() + " x " + detail.getNumber() + ", ";
            }
            order.setOrderDishes(dishes.substring(0, dishes.length()-2));
            order.setOrderDetailList(null);
        }
        res.setRecords(orderVOs.getResult());
        return Result.success(res);
    }

    /**
     * 返回各类订单的数量
     * @return
     */
    @Override
    public Result<Object> statistics() {
        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
        Integer toBeConfirmed = ordersMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = ordersMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = ordersMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 将查询出的数据封装到orderStatisticsVO中响应
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return Result.success(orderStatisticsVO);
    }

    @Override
    public Result<Object> getOrderDetails(Long orderId) {
        // 查询order表获得Order属性
        OrderVO orderVO = ordersMapper.selectOneByOrderId(orderId);
        // 查询order_detail表获得orderDetailList
        orderVO.setOrderDetailList(orderDetailMapper.selectBatchByOrderId(orderId));
        // 拼装字符串，获得orderDishes
        List<String> dishes = orderVO.getOrderDetailList().stream().map(od->{
            return od.getName()+"x"+od.getNumber();
        }).collect(Collectors.toList());
        orderVO.setOrderDishes(String.join(", ", dishes));
        return Result.success(orderVO);
    }

    @Override
    public Result<Object> confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        ordersMapper.update(orders);
        return Result.success();
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.selectOneByOrderId(ordersRejectionDTO.getId());

        // 订单只有存在且状态为2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            log.info("用户申请退款");
        }

        // 拒单需要退款，根据订单id更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        ordersMapper.update(orders);
    }

    @Override
    public void delivery(Long id) {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.selectOneByOrderId(id);

        // 校验订单是否存在，并且状态为3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        ordersMapper.update(orders);
    }

    @Override
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = ordersMapper.selectOneByOrderId(id);

        // 校验订单是否存在，并且状态为4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        ordersMapper.update(orders);
    }

    @Override
    public void checkOutOfRange(String address) {
        Map map = new HashMap();
        map.put("address",shopAddress);
        map.put("output","json");
        map.put("ak",ak);

        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("店铺地址解析失败");
        }

        //数据解析
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店铺经纬度坐标
        String shopLngLat = lat + "," + lng;

        map.put("address",address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSON.parseObject(userCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("收货地址解析失败");
        }

        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat = lat + "," + lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);
        map.put("steps_info","0");

        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);

        jsonObject = JSON.parseObject(json);
        if(!jsonObject.getString("status").equals("0")){
            throw new OrderBusinessException("配送路线规划失败");
        }

        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        if(distance > 5000){
            //配送距离超过5000米
            throw new OrderBusinessException("超出配送范围");
        }
    }

    @Override
    public void reminder(Long id) {
        // 先判断指定订单是否存在
        OrderVO orderVO = ordersMapper.selectOneByOrderId(id);
        if (orderVO == null) throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        map.put("orderId", id.toString());
        map.put("content", "用户催单了");
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }
}
