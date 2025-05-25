package com.cf.cloudflare_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateFrequencyRequest {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "频次不能为空")
    @Min(value = 0, message = "频次不能小于0")
    private Integer frequency;
    
    // 构造函数
    public UpdateFrequencyRequest() {
    }
    
    public UpdateFrequencyRequest(Long userId, Integer frequency) {
        this.userId = userId;
        this.frequency = frequency;
    }
    
    // Getter和Setter方法
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getFrequency() {
        return frequency;
    }
    
    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
