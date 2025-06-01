package com.cf.cloudflare_api.controller;

import com.cf.cloudflare_api.dto.ApiResponse;
import com.cf.cloudflare_api.service.CloudflareService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Cloudflare API 代理控制器
 * 解决前端直接调用Cloudflare API的跨域问题
 */
@RestController
@RequestMapping("/api/cloudflare")
public class CloudflareProxyController {

    private final CloudflareService cloudflareService;

    public CloudflareProxyController(CloudflareService cloudflareService) {
        this.cloudflareService = cloudflareService;
    }

    /**
     * 获取邮件路由规则
     * GET /api/cloudflare/zones/{zoneId}/email/routing/rules
     */
    @GetMapping("/zones/{zoneId}/email/routing/rules")
    public ResponseEntity<String> getEmailRoutingRules(
            @PathVariable String zoneId,
            @RequestHeader("Authorization") String authorization) {
        
        // 提取Bearer token
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.getEmailRoutingRules(apiToken, zoneId);
    }

    /**
     * 创建邮件路由规则
     * POST /api/cloudflare/zones/{zoneId}/email/routing/rules
     */
    @PostMapping("/zones/{zoneId}/email/routing/rules")
    public ResponseEntity<String> createEmailRoutingRule(
            @PathVariable String zoneId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, Object> ruleData) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.createEmailRoutingRule(apiToken, zoneId, ruleData);
    }

    /**
     * 更新邮件路由规则
     * PUT /api/cloudflare/zones/{zoneId}/email/routing/rules/{ruleId}
     */
    @PutMapping("/zones/{zoneId}/email/routing/rules/{ruleId}")
    public ResponseEntity<String> updateEmailRoutingRule(
            @PathVariable String zoneId,
            @PathVariable String ruleId,
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, Object> ruleData) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.updateEmailRoutingRule(apiToken, zoneId, ruleId, ruleData);
    }

    /**
     * 删除邮件路由规则
     * DELETE /api/cloudflare/zones/{zoneId}/email/routing/rules/{ruleId}
     */
    @DeleteMapping("/zones/{zoneId}/email/routing/rules/{ruleId}")
    public ResponseEntity<String> deleteEmailRoutingRule(
            @PathVariable String zoneId,
            @PathVariable String ruleId,
            @RequestHeader("Authorization") String authorization) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.deleteEmailRoutingRule(apiToken, zoneId, ruleId);
    }

    /**
     * 获取区域信息
     * GET /api/cloudflare/zones/{zoneId}
     */
    @GetMapping("/zones/{zoneId}")
    public ResponseEntity<String> getZoneInfo(
            @PathVariable String zoneId,
            @RequestHeader("Authorization") String authorization) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.getZoneInfo(apiToken, zoneId);
    }

    /**
     * 获取所有区域
     * GET /api/cloudflare/zones
     */
    @GetMapping("/zones")
    public ResponseEntity<String> getZones(
            @RequestHeader("Authorization") String authorization) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        return cloudflareService.getZones(apiToken);
    }

    /**
     * 通用代理接口
     * 支持任意Cloudflare API端点
     */
    @RequestMapping("/proxy/**")
    public ResponseEntity<String> proxyCloudflareApi(
            @RequestHeader("Authorization") String authorization,
            @RequestBody(required = false) Map<String, Object> requestData,
            HttpMethod method,
            @RequestParam(required = false) String endpoint) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"无效的Authorization头\"}"
            );
        }
        
        if (endpoint == null || endpoint.isEmpty()) {
            return ResponseEntity.badRequest().body(
                "{\"success\":false,\"error\":\"缺少endpoint参数\"}"
            );
        }
        
        return cloudflareService.proxyCloudflareApi(apiToken, endpoint, method, requestData);
    }

    /**
     * 测试Cloudflare API连接
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> testConnection(
            @RequestHeader("Authorization") String authorization) {
        
        String apiToken = extractBearerToken(authorization);
        if (apiToken == null) {
            return ResponseEntity.badRequest().body(
                ApiResponse.error(400, "无效的Authorization头")
            );
        }
        
        try {
            ResponseEntity<String> response = cloudflareService.getZones(apiToken);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(ApiResponse.success("Cloudflare API连接测试成功", "连接正常"));
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(
                    ApiResponse.error(response.getStatusCode().value(), "Cloudflare API连接失败")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                ApiResponse.error(500, "连接测试失败: " + e.getMessage())
            );
        }
    }

    /**
     * 从Authorization头中提取Bearer token
     */
    private String extractBearerToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}
