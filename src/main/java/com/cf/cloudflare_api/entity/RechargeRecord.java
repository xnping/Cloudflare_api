package com.cf.cloudflare_api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

@TableName("recharge_record")
public class RechargeRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId; // 充值用户ID
    
    private String cardCode; // 使用的卡密
    
    private Integer amount; // 充值数量
    
    private String type; // 充值类型：card-卡密充值, admin-管理员充值
    
    private Integer beforeBalance; // 充值前余额
    
    private Integer afterBalance; // 充值后余额
    
    private String description; // 充值描述
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    // 构造函数
    public RechargeRecord() {
    }
    
    public RechargeRecord(Long userId, String cardCode, Integer amount, String type, 
                         Integer beforeBalance, Integer afterBalance, String description) {
        this.userId = userId;
        this.cardCode = cardCode;
        this.amount = amount;
        this.type = type;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.description = description;
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
