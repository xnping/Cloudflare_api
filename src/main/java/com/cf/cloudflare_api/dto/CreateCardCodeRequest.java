package com.cf.cloudflare_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateCardCodeRequest {
    
    @NotNull(message = "面值不能为空")
    @Min(value = 1, message = "面值必须大于0")
    private Integer value;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer count; // 生成卡密的数量
    
    private Integer validDays; // 有效天数，null表示永久有效
    
    private String description; // 卡密描述
    
    // 构造函数
    public CreateCardCodeRequest() {
    }
    
    public CreateCardCodeRequest(Integer value, Integer count, Integer validDays, String description) {
        this.value = value;
        this.count = count;
        this.validDays = validDays;
        this.description = description;
    }
    
    // Getter和Setter方法
    public Integer getValue() {
        return value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Integer getValidDays() {
        return validDays;
    }
    
    public void setValidDays(Integer validDays) {
        this.validDays = validDays;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
