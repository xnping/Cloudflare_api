package com.cf.cloudflare_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePermissionsRequest {

    @NotNull(message = "用户ID不能为空")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "权限不能为空")
    private String permissions; // 'user' 或 'admin'

    // Getter methods
    public Long getUserId() {
        return userId;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getValidatedPermissions() {
        return "admin".equalsIgnoreCase(permissions) ? "admin" : "user";
    }
}
