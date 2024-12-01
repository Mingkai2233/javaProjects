package org.example.event;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLonginEvent {
    private String username;

}
