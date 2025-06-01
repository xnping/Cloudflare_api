package com.cf.cloudflare_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Cloudflare API 代理服务
 * 解决前端直接调用Cloudflare API的跨域问题
 */
@Service
public class CloudflareService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Cloudflare API 基础URL
    private static final String CLOUDFLARE_API_BASE = "https://api.cloudflare.com/client/v4";

    public CloudflareService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 创建HTTP请求头
     */
    private HttpHeaders createHeaders(String apiToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);
        headers.set("User-Agent", "CloudflareAPI-Proxy/1.0");
        return headers;
    }

    /**
     * 获取邮件路由规则
     */
    public ResponseEntity<String> getEmailRoutingRules(String apiToken, String zoneId) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones/" + zoneId + "/email/routing/rules";
            
            HttpHeaders headers = createHeaders(apiToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"调用Cloudflare API失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 创建邮件路由规则
     */
    public ResponseEntity<String> createEmailRoutingRule(String apiToken, String zoneId, Map<String, Object> ruleData) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones/" + zoneId + "/email/routing/rules";
            
            HttpHeaders headers = createHeaders(apiToken);
            String requestBody = objectMapper.writeValueAsString(ruleData);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"创建邮件路由规则失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 更新邮件路由规则
     */
    public ResponseEntity<String> updateEmailRoutingRule(String apiToken, String zoneId, String ruleId, Map<String, Object> ruleData) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones/" + zoneId + "/email/routing/rules/" + ruleId;
            
            HttpHeaders headers = createHeaders(apiToken);
            String requestBody = objectMapper.writeValueAsString(ruleData);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"更新邮件路由规则失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 删除邮件路由规则
     */
    public ResponseEntity<String> deleteEmailRoutingRule(String apiToken, String zoneId, String ruleId) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones/" + zoneId + "/email/routing/rules/" + ruleId;
            
            HttpHeaders headers = createHeaders(apiToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"删除邮件路由规则失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 获取区域信息
     */
    public ResponseEntity<String> getZoneInfo(String apiToken, String zoneId) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones/" + zoneId;
            
            HttpHeaders headers = createHeaders(apiToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"获取区域信息失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 获取所有区域
     */
    public ResponseEntity<String> getZones(String apiToken) {
        try {
            String url = CLOUDFLARE_API_BASE + "/zones";
            
            HttpHeaders headers = createHeaders(apiToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"获取区域列表失败: " + e.getMessage() + "\"}"
            );
        }
    }

    /**
     * 通用的Cloudflare API代理方法
     */
    public ResponseEntity<String> proxyCloudflareApi(String apiToken, String endpoint, HttpMethod method, Map<String, Object> requestData) {
        try {
            String url = CLOUDFLARE_API_BASE + endpoint;
            
            HttpHeaders headers = createHeaders(apiToken);
            String requestBody = requestData != null ? objectMapper.writeValueAsString(requestData) : null;
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, method, entity, String.class
            );
            
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "{\"success\":false,\"error\":\"调用Cloudflare API失败: " + e.getMessage() + "\"}"
            );
        }
    }
}
