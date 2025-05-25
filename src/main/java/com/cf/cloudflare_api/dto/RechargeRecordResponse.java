package com.cf.cloudflare_api.dto;

import com.cf.cloudflare_api.entity.RechargeRecord;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class RechargeRecordResponse {
    
    private Long id;
    private Long userId;
    private String username; // 用户名
    private String cardCode;
    private Integer amount;
    private String type;
    private Integer beforeBalance;
    private Integer afterBalance;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // 构造函数
    public RechargeRecordResponse() {
    }
    
    // 从RechargeRecord实体转换
    public static RechargeRecordResponse fromRechargeRecord(RechargeRecord record) {
        RechargeRecordResponse response = new RechargeRecordResponse();
        response.setId(record.getId());
        response.setUserId(record.getUserId());
        response.setCardCode(record.getCardCode());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setBeforeBalance(record.getBeforeBalance());
        response.setAfterBalance(record.getAfterBalance());
        response.setDescription(record.getDescription());
        response.setCreatedAt(record.getCreatedAt());
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getCardCode() {
        return cardCode;
    }
    
    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getBeforeBalance() {
        return beforeBalance;
    }
    
    public void setBeforeBalance(Integer beforeBalance) {
        this.beforeBalance = beforeBalance;
    }
    
    public Integer getAfterBalance() {
        return afterBalance;
    }
    
    public void setAfterBalance(Integer afterBalance) {
        this.afterBalance = afterBalance;
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
}
