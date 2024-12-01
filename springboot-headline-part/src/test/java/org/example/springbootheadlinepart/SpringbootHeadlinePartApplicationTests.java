package org.example.springbootheadlinepart;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootheadlinepart.util.MD5Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@Slf4j
@SpringBootTest
class SpringbootHeadlinePartApplicationTests {
    @Test
    void contextLoads() {
        String password="123456";
        Assertions.assertEquals(MD5Util.encrypt(password), "e10adc3949ba59abbe56e057f20f883e"  );
    }

}
