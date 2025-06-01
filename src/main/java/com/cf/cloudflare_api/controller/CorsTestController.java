package com.cf.cloudflare_api.controller;

import com.cf.cloudflare_api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 跨域测试控制器
 * 用于测试CORS配置是否正常工作
 */
@RestController
@RequestMapping("/api/cors-test")
public class CorsTestController {

    /**
     * 简单的GET请求测试
     */
    @GetMapping("/simple")
    public ResponseEntity<ApiResponse<Map<String, Object>>> simpleTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS GET请求测试成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "GET");
        
        return ResponseEntity.ok(ApiResponse.success("跨域GET测试成功", data));
    }

    /**
     * POST请求测试
     */
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<Map<String, Object>>> postTest(@RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS POST请求测试成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "POST");
        data.put("receivedData", requestData);
        
        return ResponseEntity.ok(ApiResponse.success("跨域POST测试成功", data));
    }

    /**
     * PUT请求测试
     */
    @PutMapping("/put")
    public ResponseEntity<ApiResponse<Map<String, Object>>> putTest(@RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS PUT请求测试成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "PUT");
        data.put("receivedData", requestData);
        
        return ResponseEntity.ok(ApiResponse.success("跨域PUT测试成功", data));
    }

    /**
     * DELETE请求测试
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS DELETE请求测试成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "DELETE");
        
        return ResponseEntity.ok(ApiResponse.success("跨域DELETE测试成功", data));
    }

    /**
     * 带Authorization头的请求测试
     */
    @GetMapping("/with-auth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> authTest(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS带认证头请求测试成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "GET");
        data.put("authHeader", authHeader != null ? "已接收到Authorization头" : "未接收到Authorization头");
        
        return ResponseEntity.ok(ApiResponse.success("跨域认证测试成功", data));
    }

    /**
     * OPTIONS预检请求测试
     */
    @RequestMapping(value = "/preflight", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> preflightTest() {
        return ResponseEntity.ok().build();
    }

    /**
     * 获取CORS配置信息
     */
    @GetMapping("/config")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCorsConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("allowedOrigins", "所有源 (*)");
        config.put("allowedMethods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        config.put("allowedHeaders", "Authorization, Content-Type, X-Requested-With, Accept, Origin, etc.");
        config.put("allowCredentials", true);
        config.put("maxAge", 3600);
        config.put("status", "CORS已配置");
        
        return ResponseEntity.ok(ApiResponse.success("CORS配置信息", config));
    }
}
