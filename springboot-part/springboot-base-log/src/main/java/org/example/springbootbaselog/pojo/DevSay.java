package org.example.springbootbaselog.pojo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Profile("dev")
@Component
public class DevSay implements Say{
    @Override
    public void say() {
        System.out.println("Hello" + this.getClass().getName() + "!");

    }
}
