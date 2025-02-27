package npu.edu.redisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisDemoApplicationTests {
	@Autowired
	RedisTemplate redisTemplate;
	@Test
	void contextLoads() {
		redisTemplate.opsForValue().set("name", "胡歌");
		System.out.println(redisTemplate.opsForValue().get("name"));
	}

}
