package com.cf.cloudflare_api.dto;

import jakarta.validation.constraints.NotBlank;

public class UseCardCodeRequest {
    
    @NotBlank(message = "卡密不能为空")
    private String code;
    
    // 构造函数
    public UseCardCodeRequest() {
    }
    
    public UseCardCodeRequest(String code) {
        this.code = code;
    }
    
    // Getter和Setter方法
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
}
