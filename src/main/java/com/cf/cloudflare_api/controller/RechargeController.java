package com.cf.cloudflare_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.ApiResponse;
import com.cf.cloudflare_api.dto.RechargeRecordResponse;
import com.cf.cloudflare_api.dto.UseCardCodeRequest;
import com.cf.cloudflare_api.dto.UserResponse;
import com.cf.cloudflare_api.entity.RechargeRecord;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.service.RechargeService;
import com.cf.cloudflare_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharge")
public class RechargeController {
    
    private final RechargeService rechargeService;
    private final UserService userService;
    
    public RechargeController(RechargeService rechargeService, UserService userService) {
        this.rechargeService = rechargeService;
        this.userService = userService;
    }
    
    /**
     * 使用卡密充值
     */
    @PostMapping("/card")
    public ResponseEntity<ApiResponse<UserResponse>> useCardCode(
            @Valid @RequestBody UseCardCodeRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            rechargeService.useCardCode(currentUser.getId(), request);
            
            // 获取更新后的用户信息
            User updatedUser = userService.findById(currentUser.getId());
            return ResponseEntity.ok(ApiResponse.success("卡密充值成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * 管理员充值
     */
    @PostMapping("/admin")
    public ResponseEntity<ApiResponse<UserResponse>> adminRecharge(
            @RequestParam Long userId,
            @RequestParam Integer amount,
            @RequestParam(required = false) String description,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以进行管理员充值
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以进行充值操作"));
            }
            
            rechargeService.adminRecharge(userId, amount, description);
            
            // 获取更新后的用户信息
            User updatedUser = userService.findById(userId);
            return ResponseEntity.ok(ApiResponse.success("管理员充值成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            if (e.getMessage().contains("用户不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 获取当前用户充值记录
     */
    @GetMapping("/records")
    public ResponseEntity<ApiResponse<List<RechargeRecordResponse>>> getUserRechargeRecords(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            List<RechargeRecord> records = rechargeService.getUserRechargeRecords(currentUser.getId());
            List<RechargeRecordResponse> responses = records.stream()
                    .map(record -> {
                        RechargeRecordResponse response = RechargeRecordResponse.fromRechargeRecord(record);
                        response.setUsername(currentUser.getUsername());
                        return response;
                    })
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取充值记录成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 获取指定用户充值记录（仅管理员）
     */
    @GetMapping("/records/{userId}")
    public ResponseEntity<ApiResponse<List<RechargeRecordResponse>>> getUserRechargeRecordsById(
            @PathVariable Long userId,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以查看其他用户的充值记录
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看其他用户的充值记录"));
            }
            
            User targetUser = userService.findById(userId);
            if (targetUser == null) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, "用户不存在"));
            }
            
            List<RechargeRecord> records = rechargeService.getUserRechargeRecords(userId);
            List<RechargeRecordResponse> responses = records.stream()
                    .map(record -> {
                        RechargeRecordResponse response = RechargeRecordResponse.fromRechargeRecord(record);
                        response.setUsername(targetUser.getUsername());
                        return response;
                    })
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取用户充值记录成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 获取所有充值记录（仅管理员）
     */
    @GetMapping("/records/all")
    public ResponseEntity<ApiResponse<List<RechargeRecordResponse>>> getAllRechargeRecords(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以查看所有充值记录
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看所有充值记录"));
            }
            
            List<RechargeRecord> records = rechargeService.getAllRechargeRecords();
            List<RechargeRecordResponse> responses = records.stream()
                    .map(record -> {
                        RechargeRecordResponse response = RechargeRecordResponse.fromRechargeRecord(record);
                        // 获取用户名
                        User user = userService.findById(record.getUserId());
                        if (user != null) {
                            response.setUsername(user.getUsername());
                        }
                        return response;
                    })
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取所有充值记录成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 分页获取充值记录（仅管理员）
     */
    @GetMapping("/records/page")
    public ResponseEntity<ApiResponse<Page<RechargeRecordResponse>>> getRechargeRecordsWithPagination(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以查看充值记录分页列表
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看充值记录分页列表"));
            }
            
            Page<RechargeRecord> recordPage = rechargeService.findRechargeRecordsWithPagination(pageNum, pageSize);
            
            // 转换为RechargeRecordResponse
            Page<RechargeRecordResponse> responsePage = new Page<>(pageNum, pageSize, recordPage.getTotal());
            List<RechargeRecordResponse> responses = recordPage.getRecords().stream()
                    .map(record -> {
                        RechargeRecordResponse response = RechargeRecordResponse.fromRechargeRecord(record);
                        // 获取用户名
                        User user = userService.findById(record.getUserId());
                        if (user != null) {
                            response.setUsername(user.getUsername());
                        }
                        return response;
                    })
                    .toList();
            responsePage.setRecords(responses);
            
            return ResponseEntity.ok(ApiResponse.success("获取充值记录分页列表成功", responsePage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
}
