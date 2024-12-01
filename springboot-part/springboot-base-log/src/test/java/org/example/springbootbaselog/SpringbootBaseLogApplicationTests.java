package org.example.springbootbaselog;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootbaselog.pojo.DevSay;
import org.example.springbootbaselog.pojo.TestSay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class SpringbootBaseLogApplicationTests {
    @Autowired
    TestSay say;
    @Test
    void contextLoads() {
        say.say();
    }

}
