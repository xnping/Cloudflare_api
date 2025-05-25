package com.cf.cloudflare_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "用户名或邮箱不能为空")
    private String username; // 可以是用户名或邮箱

    @NotBlank(message = "密码不能为空")
    private String password;

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
