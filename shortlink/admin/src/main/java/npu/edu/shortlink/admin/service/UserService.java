package npu.edu.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import npu.edu.shortlink.admin.dao.entity.UserDO;
import npu.edu.shortlink.admin.dto.resp.UserRespDTO;


public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);
}
