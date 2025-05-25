package com.cf.cloudflare_api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.RegisterRequest;
import com.cf.cloudflare_api.dto.UpdatePermissionsRequest;
import com.cf.cloudflare_api.dto.UpdateFrequencyRequest;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.mapper.UserMapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFrequency(0);
        user.setPermissions(request.getValidatedPermissions());

        userMapper.insert(user);
        return user;
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userMapper.findByUsernameOrEmail(usernameOrEmail);
    }

    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    public List<User> findAllUsers() {
        return userMapper.selectList(null);
    }

    public User updateUserPermissions(UpdatePermissionsRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPermissions(request.getValidatedPermissions());
        userMapper.updateById(user);
        return user;
    }

    public User updateUserFrequency(UpdateFrequencyRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setFrequency(request.getFrequency());
        userMapper.updateById(user);
        return user;
    }

    public User incrementUserFrequency(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setFrequency(user.getFrequency() + 1);
        userMapper.updateById(user);
        return user;
    }

    public User decrementUserFrequency(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查frequency是否足够
        if (user.getFrequency() <= 0) {
            throw new RuntimeException("用户剩余次数不足，无法创建邮箱记录");
        }

        user.setFrequency(user.getFrequency() - 1);
        userMapper.updateById(user);
        return user;
    }

    /**
     * 分页查询用户
     */
    public Page<User> findUsersWithPagination(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page, null);
    }

    /**
     * 更新用户基本信息
     */
    public User updateUser(Long id, RegisterRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否被其他用户使用
        User existingUser = userMapper.findByUsername(request.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new RuntimeException("用户名已被其他用户使用");
        }

        // 检查邮箱是否被其他用户使用
        existingUser = userMapper.findByEmail(request.getEmail());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new RuntimeException("邮箱已被其他用户使用");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setPermissions(request.getValidatedPermissions());

        userMapper.updateById(user);
        return user;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return userMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除用户
     */
    public boolean deleteUsers(List<Long> ids) {
        return userMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 更新用户实体（用于内部调用）
     */
    public User updateUser(Long id, User user) {
        userMapper.updateById(user);
        return user;
    }
}
