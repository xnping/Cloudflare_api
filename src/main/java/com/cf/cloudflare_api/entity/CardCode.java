package com.cf.cloudflare_api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

@TableName("card_code")
public class CardCode {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String code; // 卡密码
    
    private Integer value; // 面值（充值的frequency数量）
    
    private String status; // 状态：unused-未使用, used-已使用, disabled-已禁用
    
    private Long usedByUserId; // 使用者用户ID
    
    private LocalDateTime usedAt; // 使用时间
    
    private LocalDateTime expiresAt; // 过期时间
    
    private String description; // 卡密描述
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 构造函数
    public CardCode() {
    }
    
    public CardCode(String code, Integer value, LocalDateTime expiresAt, String description) {
        this.code = code;
        this.value = value;
        this.expiresAt = expiresAt;
        this.description = description;
        this.status = "unused";
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
    
    // 业务方法
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isUsable() {
        return "unused".equals(status) && !isExpired();
    }
}
