package org.example.springbootheadlinepart.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class User implements Serializable {
    
    private Integer uid;
    @NotBlank
    @NotNull()
    private String username;
    @NotBlank
    @NotNull()
    private String userPwd;
    private String nickName;
    private Integer version;
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}