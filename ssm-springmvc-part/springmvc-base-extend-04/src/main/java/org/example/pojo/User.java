package org.example.pojo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class User {
    @NotBlank
    private String name;

    @Min(value = 1)
    @Max(value = 120)
    private int age;
    @Length(min = 5, max = 12)
    private String account;
    @Length(min = 5, max = 20)
    private String password;
}
