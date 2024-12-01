package org.example.springbootbaselog.pojo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Profile("test")
@Component
public class TestSay implements Say{
    @Override
    public void say() {
        System.out.println("Hello" + this.getClass().getName() + "!");

    }
}
