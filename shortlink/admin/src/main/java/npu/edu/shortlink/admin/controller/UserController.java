package npu.edu.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import npu.edu.shortlink.admin.common.convention.result.Result;
import npu.edu.shortlink.admin.common.convention.result.Results;
import npu.edu.shortlink.admin.dto.req.UserRegisterReqDTO;
import npu.edu.shortlink.admin.dto.resp.UserActualRespDTO;
import npu.edu.shortlink.admin.dto.resp.UserRespDTO;
import npu.edu.shortlink.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    // 使用lombok的全参数构造函数注入
    private final UserService userService;
    /**
     * 根据用户名查询用户脱敏信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }

    /**
     * 查询用户名是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }
}
