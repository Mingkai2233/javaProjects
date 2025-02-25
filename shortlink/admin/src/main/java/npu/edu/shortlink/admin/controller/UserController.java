package npu.edu.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import npu.edu.shortlink.admin.common.convention.result.Result;
import npu.edu.shortlink.admin.common.convention.result.Results;
import npu.edu.shortlink.admin.dto.resp.UserRespDTO;
import npu.edu.shortlink.admin.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }
}
