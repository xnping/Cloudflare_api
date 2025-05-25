package com.cf.cloudflare_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.ApiResponse;
import com.cf.cloudflare_api.dto.RegisterRequest;
import com.cf.cloudflare_api.dto.UpdatePermissionsRequest;
import com.cf.cloudflare_api.dto.UpdateFrequencyRequest;
import com.cf.cloudflare_api.dto.UserResponse;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/info")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            User user = userService.findById(currentUser.getId());

            if (user == null) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, "用户不存在"));
            }

            return ResponseEntity.ok(ApiResponse.success("获取成功", UserResponse.fromUser(user)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证当前用户是否为管理员
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看所有用户"));
            }

            List<User> users = userService.findAllUsers();
            List<UserResponse> userResponses = users.stream()
                    .map(UserResponse::fromUser)
                    .toList();

            return ResponseEntity.ok(ApiResponse.success("获取用户列表成功", userResponses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @PutMapping("/user/permissions")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserPermissions(
            @Valid @RequestBody UpdatePermissionsRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证当前用户是否为管理员
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以修改用户权限"));
            }

            User updatedUser = userService.updateUserPermissions(request);
            return ResponseEntity.ok(ApiResponse.success("用户权限更新成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            if (e.getMessage().contains("用户不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @PutMapping("/user/frequency")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserFrequency(
            @Valid @RequestBody UpdateFrequencyRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：只有管理员或用户本人可以修改frequency
            if (!"admin".equals(currentUser.getPermissions()) && !currentUser.getId().equals(request.getUserId())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只能修改自己的使用频次"));
            }

            User updatedUser = userService.updateUserFrequency(request);
            return ResponseEntity.ok(ApiResponse.success("用户使用频次更新成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            if (e.getMessage().contains("用户不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @PostMapping("/user/{userId}/frequency/increment")
    public ResponseEntity<ApiResponse<UserResponse>> incrementUserFrequency(
            @PathVariable Long userId,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：只有管理员可以增加frequency（充值功能）
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以为用户充值次数"));
            }

            User updatedUser = userService.incrementUserFrequency(userId);
            return ResponseEntity.ok(ApiResponse.success("用户使用频次充值成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            if (e.getMessage().contains("用户不存在")) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @GetMapping("/users/page")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersWithPagination(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证当前用户是否为管理员
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以查看用户分页列表"));
            }

            Page<User> userPage = userService.findUsersWithPagination(pageNum, pageSize);

            // 转换为UserResponse
            Page<UserResponse> responsePage = new Page<>(pageNum, pageSize, userPage.getTotal());
            List<UserResponse> responses = userPage.getRecords().stream()
                    .map(UserResponse::fromUser)
                    .toList();
            responsePage.setRecords(responses);

            return ResponseEntity.ok(ApiResponse.success("获取用户分页列表成功", responsePage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：管理员或用户本人
            if (!"admin".equals(currentUser.getPermissions()) && !currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只能查看自己的信息"));
            }

            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.status(404).body(ApiResponse.error(404, "用户不存在"));
            }

            return ResponseEntity.ok(ApiResponse.success("获取用户信息成功", UserResponse.fromUser(user)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：管理员或用户本人
            if (!"admin".equals(currentUser.getPermissions()) && !currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只能修改自己的信息"));
            }

            User updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("用户信息更新成功", UserResponse.fromUser(updatedUser)));
        } catch (Exception e) {
            if (e.getMessage().contains("不存在") || e.getMessage().contains("已被")) {
                return ResponseEntity.status(400).body(ApiResponse.error(400, e.getMessage()));
            }
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：只有管理员可以删除用户
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以删除用户"));
            }

            // 不能删除自己
            if (currentUser.getId().equals(id)) {
                return ResponseEntity.status(400).body(ApiResponse.error(400, "不能删除自己的账户"));
            }

            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
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

    @DeleteMapping("/users/batch")
    public ResponseEntity<ApiResponse<Void>> deleteUsers(
            @RequestBody List<Long> ids,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();

            // 验证权限：只有管理员可以批量删除用户
            if (!"admin".equals(currentUser.getPermissions())) {
                return ResponseEntity.status(403).body(ApiResponse.error(403, "权限不足，只有管理员可以批量删除用户"));
            }

            // 检查是否包含自己的ID
            if (ids.contains(currentUser.getId())) {
                return ResponseEntity.status(400).body(ApiResponse.error(400, "不能删除自己的账户"));
            }

            boolean deleted = userService.deleteUsers(ids);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("批量删除用户成功"));
            } else {
                return ResponseEntity.status(500).body(ApiResponse.error(500, "批量删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error(500, "服务器错误: " + e.getMessage()));
        }
    }
}
