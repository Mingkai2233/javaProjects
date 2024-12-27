package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.constant.ErrorMessageConstant;
import com.hmdp.constant.RedisConstants;
import com.hmdp.constant.SessionConstant;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.exception.CommonException;
import com.hmdp.exception.PhoneNumInvalid;
import com.hmdp.exception.UnknownError;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public UserServiceImpl(StringRedisTemplate sredisTemplate) {
        this.stringRedisTemplate = sredisTemplate;
    }
    @Override
    public Result sendCode(String phone, HttpSession session) {
        // 1. 校验手机号
        boolean isValid = RegexUtils.isPhoneInvalid(phone);
        if (isValid) throw new PhoneNumInvalid(ErrorMessageConstant.PHONE_NUM_INVALID);
        // 2. 生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 3. 保存验证码 和 手机号 存入redis中 login:code:phone code
//        session.setAttribute(SessionConstant.USER_CODE, code);
//        session.setAttribute(SessionConstant.USER_PHONE_NUM, phone);
        String redisKey = RedisConstants.LOGIN_CODE_KEY+phone;
        stringRedisTemplate.opsForValue().set(redisKey, code);
        stringRedisTemplate.expire(redisKey, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 4. 发送验证码
        log.debug("验证码发送成功，验证码：{}", code);
        return Result.ok();
    }

    private User createUserWithPhone(String phone){
        User user = new User();
        user.setPhone(phone);
        user.setNickName("user_"+RandomUtil.randomString(10));
        save(user);
        return user;
    }
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1. 校验手机号和验证码
//        String phone = session.getAttribute(SessionConstant.USER_PHONE_NUM).toString();
//        String code = session.getAttribute(SessionConstant.USER_CODE).toString();
//        if (phone == null || code == null) throw new UnknownError("登录失败");
//        if (!phone.equals(loginForm.getPhone())||!code.equals(loginForm.getCode()))
//            throw new PhoneNumInvalid(ErrorMessageConstant.CODE_INVALID);
        String phone = loginForm.getPhone();
        String code = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY+loginForm.getPhone());
        if(code == null) throw new CommonException(ErrorMessageConstant.CODE_OR_PHONE_NUM_INVALID);
        if(!code.equals(loginForm.getCode())) throw new CommonException(ErrorMessageConstant.CODE_INVALID);
        // 2. 判断用户是否存在
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getPhone, phone);
        User user = getOne(qw);
        // 3. 不存在则创建用户
        if (user == null) user = createUserWithPhone(phone);
        // 4. 保存用户到redis
//        session.setAttribute(SessionConstant.USER, user);
        // 4.1 随机生成token，作为登录令牌
        String token = IdUtil.simpleUUID();
        // 4.2 将User对象转为HashMap存储
        String redisKey = RedisConstants.LOGIN_USER_KEY+ token;
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        // 4.3 存储并设置过期时间
        stringRedisTemplate.opsForHash().putAll(redisKey, userMap);
        stringRedisTemplate.expire(redisKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return Result.ok(token);
    }
}
