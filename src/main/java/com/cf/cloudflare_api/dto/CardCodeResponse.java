package com.cf.cloudflare_api.dto;

import com.cf.cloudflare_api.entity.CardCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class CardCodeResponse {
    
    private Long id;
    private String code;
    private Integer value;
    private String status;
    private Long usedByUserId;
    private String usedByUsername; // 使用者用户名
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // 构造函数
    public CardCodeResponse() {
    }
    
    // 从CardCode实体转换
    public static CardCodeResponse fromCardCode(CardCode cardCode) {
        CardCodeResponse response = new CardCodeResponse();
        response.setId(cardCode.getId());
        response.setCode(cardCode.getCode());
        response.setValue(cardCode.getValue());
        response.setStatus(cardCode.getStatus());
        response.setUsedByUserId(cardCode.getUsedByUserId());
        response.setUsedAt(cardCode.getUsedAt());
        response.setExpiresAt(cardCode.getExpiresAt());
        response.setDescription(cardCode.getDescription());
        response.setCreatedAt(cardCode.getCreatedAt());
        response.setUpdatedAt(cardCode.getUpdatedAt());
        return response;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getUsedByUserId() {
        return usedByUserId;
    }
    
    public void setUsedByUserId(Long usedByUserId) {
        this.usedByUserId = usedByUserId;
    }
    
    public String getUsedByUsername() {
        return usedByUsername;
    }
    
    public void setUsedByUsername(String usedByUsername) {
        this.usedByUsername = usedByUsername;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
