package org.example.servcie;

import lombok.extern.slf4j.Slf4j;
import org.example.event.UserLonginEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class UserService {
    @EventListener(UserLonginEvent.class)
    public void grantPoints(UserLonginEvent event){
        log.info("给用户{}发放积分", event.getUsername());
    }
}
