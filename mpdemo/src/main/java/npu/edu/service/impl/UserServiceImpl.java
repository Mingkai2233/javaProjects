package npu.edu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import npu.edu.entity.User;
import npu.edu.mapper.UserMapper;
import npu.edu.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
