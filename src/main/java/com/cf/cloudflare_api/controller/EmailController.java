package com.cf.cloudflare_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.ApiResponse;
import com.cf.cloudflare_api.dto.EmailRequest;
import com.cf.cloudflare_api.dto.EmailResponse;
import com.cf.cloudflare_api.entity.Email;
import com.cf.cloudflare_api.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {
    
    private final EmailService emailService;
    
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    
    /**
     * 创建邮箱记录
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EmailResponse>> createEmail(@Valid @RequestBody EmailRequest request) {
        try {
            Email email = emailService.createEmail(request);
            EmailResponse response = EmailResponse.fromEmail(email);
            return ResponseEntity.ok(ApiResponse.success("邮箱记录创建成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
    
    /**
     * 根据ID获取邮箱记录
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmailResponse>> getEmailById(@PathVariable Long id) {
        try {
            Email email = emailService.findById(id);
            if (email == null) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, "邮箱记录不存在"));
            }
            
            EmailResponse response = EmailResponse.fromEmail(email);
            return ResponseEntity.ok(ApiResponse.success("获取成功", response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 获取所有邮箱记录
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EmailResponse>>> getAllEmails() {
        try {
            List<Email> emails = emailService.findAllEmails();
            List<EmailResponse> responses = emails.stream()
                    .map(EmailResponse::fromEmail)
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取邮箱列表成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 分页获取邮箱记录
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<EmailResponse>>> getEmailsWithPagination(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<Email> emailPage = emailService.findEmailsWithPagination(pageNum, pageSize);
            
            // 转换为EmailResponse
            Page<EmailResponse> responsePage = new Page<>(pageNum, pageSize, emailPage.getTotal());
            List<EmailResponse> responses = emailPage.getRecords().stream()
                    .map(EmailResponse::fromEmail)
                    .toList();
            responsePage.setRecords(responses);
            
            return ResponseEntity.ok(ApiResponse.success("获取邮箱分页列表成功", responsePage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 根据用户ID获取邮箱记录
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<EmailResponse>>> getEmailsByUserId(@PathVariable Long userId) {
        try {
            List<Email> emails = emailService.findByUserId(userId);
            List<EmailResponse> responses = emails.stream()
                    .map(EmailResponse::fromEmail)
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success("获取用户邮箱列表成功", responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 更新邮箱记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmailResponse>> updateEmail(
            @PathVariable Long id, 
            @Valid @RequestBody EmailRequest request) {
        try {
            Email email = emailService.updateEmail(id, request);
            EmailResponse response = EmailResponse.fromEmail(email);
            return ResponseEntity.ok(ApiResponse.success("邮箱记录更新成功", response));
        } catch (Exception e) {
            if (e.getMessage().contains("不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
    
    /**
     * 删除邮箱记录
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmail(@PathVariable Long id) {
        try {
            boolean deleted = emailService.deleteEmail(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("邮箱记录删除成功"));
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
     * 批量删除邮箱记录
     */
    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> deleteEmails(@RequestBody List<Long> ids) {
        try {
            boolean deleted = emailService.deleteEmails(ids);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("批量删除成功"));
            } else {
                return ResponseEntity.status(500).body(ApiResponse.error(500, "批量删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
}
