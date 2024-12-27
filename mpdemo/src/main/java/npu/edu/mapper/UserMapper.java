package npu.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import npu.edu.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    Map<String, Object> selectMapById(Long id);
}
