package com.cf.cloudflare_api.dto;

import com.cf.cloudflare_api.entity.Email;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class EmailResponse {

    private Long id;
    private Long userId;
    private String email;
    private String toEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 构造函数
    public EmailResponse() {
    }

    public EmailResponse(Long id, Long userId, String email, String toEmail) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.toEmail = toEmail;
    }

    // 从Email实体转换为EmailResponse
    public static EmailResponse fromEmail(Email email) {
        EmailResponse response = new EmailResponse();
        response.setId(email.getId());
        response.setUserId(email.getUserId());
        response.setEmail(email.getEmail());
        response.setToEmail(email.getToEmail());
        response.setCreatedAt(email.getCreatedAt());
        response.setUpdatedAt(email.getUpdatedAt());
        return response;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
