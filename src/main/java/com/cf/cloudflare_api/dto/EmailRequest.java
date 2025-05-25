package com.cf.cloudflare_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmailRequest {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "发送方邮箱不能为空")
    @Email(message = "发送方邮箱格式不正确")
    private String email;
    
    @NotBlank(message = "接收方邮箱不能为空")
    @Email(message = "接收方邮箱格式不正确")
    private String toEmail;
    
    // 构造函数
    public EmailRequest() {
    }
    
    public EmailRequest(Long userId, String email, String toEmail) {
        this.userId = userId;
        this.email = email;
        this.toEmail = toEmail;
    }
    
    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getToEmail() {
        return toEmail;
    }
    
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }
}
