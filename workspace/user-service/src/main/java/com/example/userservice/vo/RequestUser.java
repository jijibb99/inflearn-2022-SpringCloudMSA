package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email은 2자 이상")
    @Email
    private String  email;

    @NotNull(message = "비밀번호 cannot be null")
    @Size(min = 8, message = "비밀번호 최소 8자 이상")
    private String  pwd;

    @NotNull(message = "이름 cannot be null")
    @Size(min = 2, message = "이름은 2자 이상")
    private String  name;
}
