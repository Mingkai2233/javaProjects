package npu.edu.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import npu.edu.shortlink.admin.dao.entity.UserDO;
import npu.edu.shortlink.admin.dto.req.UserLoginReqDTO;
import npu.edu.shortlink.admin.dto.req.UserRegisterReqDTO;
import npu.edu.shortlink.admin.dto.req.UserUpdateReqDTO;
import npu.edu.shortlink.admin.dto.resp.UserLoginRespDTO;
import npu.edu.shortlink.admin.dto.resp.UserRespDTO;


public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO requestParam);

    void update(UserUpdateReqDTO requestParam);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);

    void logout(String username, String token);
}
