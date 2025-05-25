package com.cf.cloudflare_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.ApiResponse;
import com.cf.cloudflare_api.dto.CardCodeResponse;
import com.cf.cloudflare_api.dto.CreateCardCodeRequest;
import com.cf.cloudflare_api.entity.CardCode;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.service.CardCodeService;
import com.cf.cloudflare_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card-codes")
public class CardCodeController {
    
    private final CardCodeService cardCodeService;
    private final UserService userService;
    
    public CardCodeController(CardCodeService cardCodeService, UserService userService) {
        this.cardCodeService = cardCodeService;
        this.userService = userService;
    }
    
    /**
     * 生成卡密（仅管理员）
     */
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<List<CardCodeResponse>>> generateCardCodes(
            @Valid @RequestBody CreateCardCodeRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以生成卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以生成卡密"));
            }
            
            List<CardCode> cardCodes = cardCodeService.generateCardCodes(request);
            List<CardCodeResponse> responses = cardCodes.stream()
                    .map(CardCodeResponse::fromCardCode)
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("卡密生成成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 获取所有卡密（仅管理员）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CardCodeResponse>>> getAllCardCodes(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以查看所有卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看卡密列表"));
            }
            
            List<CardCode> cardCodes = cardCodeService.findAllCardCodes();
            List<CardCodeResponse> responses = cardCodes.stream()
                    .map(cardCode -> {
                        CardCodeResponse response = CardCodeResponse.fromCardCode(cardCode);
                        // 如果卡密已被使用，获取使用者用户名
                        if (cardCode.getUsedByUserId() != null) {
                            User user = userService.findById(cardCode.getUsedByUserId());
                            if (user != null) {
                                response.setUsedByUsername(user.getUsername());
                            }
                        }
                        return response;
                    })
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取卡密列表成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 分页获取卡密（仅管理员）
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<CardCodeResponse>>> getCardCodesWithPagination(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以查看卡密分页列表
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看卡密分页列表"));
            }
            
            Page<CardCode> cardCodePage = cardCodeService.findCardCodesWithPagination(pageNum, pageSize);
            
            // 转换为CardCodeResponse
            Page<CardCodeResponse> responsePage = new Page<>(pageNum, pageSize, cardCodePage.getTotal());
            List<CardCodeResponse> responses = cardCodePage.getRecords().stream()
                    .map(cardCode -> {
                        CardCodeResponse response = CardCodeResponse.fromCardCode(cardCode);
                        // 如果卡密已被使用，获取使用者用户名
                        if (cardCode.getUsedByUserId() != null) {
                            User user = userService.findById(cardCode.getUsedByUserId());
                            if (user != null) {
                                response.setUsedByUsername(user.getUsername());
                            }
                        }
                        return response;
                    })
                    .toList();
            responsePage.setRecords(responses);
            
            return ResponseEntity.ok(ApiResponse.success("获取卡密分页列表成功", responsePage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 禁用卡密（仅管理员）
     */
    @PutMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<CardCodeResponse>> disableCardCode(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以禁用卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以禁用卡密"));
            }
            
            CardCode cardCode = cardCodeService.disableCardCode(id);
            return ResponseEntity.ok(ApiResponse.success("卡密禁用成功", CardCodeResponse.fromCardCode(cardCode)));
        } catch (Exception e) {
            if (e.getMessage().contains("不存在") || e.getMessage().contains("已被使用")) {
                return ResponseEntity.status(400).body(ApiResponse.error(400, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 启用卡密（仅管理员）
     */
    @PutMapping("/{id}/enable")
    public ResponseEntity<ApiResponse<CardCodeResponse>> enableCardCode(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以启用卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以启用卡密"));
            }
            
            CardCode cardCode = cardCodeService.enableCardCode(id);
            return ResponseEntity.ok(ApiResponse.success("卡密启用成功", CardCodeResponse.fromCardCode(cardCode)));
        } catch (Exception e) {
            if (e.getMessage().contains("不存在") || e.getMessage().contains("已被使用")) {
                return ResponseEntity.status(400).body(ApiResponse.error(400, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 删除卡密（仅管理员）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCardCode(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以删除卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以删除卡密"));
            }
            
            boolean deleted = cardCodeService.deleteCardCode(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("卡密删除成功"));
            } else {
                return ResponseEntity.status(500).body(ApiResponse.error(500, "删除失败"));
            }
        } catch (Exception e) {
            if (e.getMessage().contains("不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 批量删除卡密（仅管理员）
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> deleteCardCodes(
            @RequestBody List<Long> ids,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以批量删除卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以批量删除卡密"));
            }
            
            boolean deleted = cardCodeService.deleteCardCodes(ids);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("批量删除卡密成功"));
            } else {
                return ResponseEntity.status(500).body(ApiResponse.error(500, "批量删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 清理过期卡密（仅管理员）
     */
    @PostMapping("/clean-expired")
    public ResponseEntity<ApiResponse<Integer>> cleanExpiredCards(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            
            // 验证权限：只有管理员可以清理过期卡密
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以清理过期卡密"));
            }
            
            int cleanedCount = cardCodeService.cleanExpiredCards();
            return ResponseEntity.ok(ApiResponse.success("清理过期卡密成功", cleanedCount));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
}
