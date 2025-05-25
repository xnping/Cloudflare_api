package com.cf.cloudflare_api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.EmailRequest;
import com.cf.cloudflare_api.entity.Email;
import com.cf.cloudflare_api.mapper.EmailMapper;
import com.cf.cloudflare_api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final EmailMapper emailMapper;
    private final UserService userService;

    public EmailService(EmailMapper emailMapper, UserService userService) {
        this.emailMapper = emailMapper;
        this.userService = userService;
    }

    /**
     * 创建邮箱记录
     */
    public Email createEmail(EmailRequest request) {
        // 检查用户是否存在
        if (userService.findById(request.getUserId()) == null) {
            throw new RuntimeException("用户不存在");
        }

        // 减少用户的frequency次数
        userService.decrementUserFrequency(request.getUserId());

        // 创建邮箱记录
        Email email = new Email();
        email.setUserId(request.getUserId());
        email.setEmail(request.getEmail());
        email.setToEmail(request.getToEmail());

        emailMapper.insert(email);
        return email;
    }

    /**
     * 根据ID查找邮箱记录
     */
    public Email findById(Long id) {
        return emailMapper.selectById(id);
    }

    /**
     * 获取所有邮箱记录
     */
    public List<Email> findAllEmails() {
        return emailMapper.selectList(null);
    }

    /**
     * 分页查询邮箱记录
     */
    public Page<Email> findEmailsWithPagination(int pageNum, int pageSize) {
        Page<Email> page = new Page<>(pageNum, pageSize);
        return emailMapper.selectPage(page, null);
    }

    /**
     * 根据用户ID查找邮箱记录
     */
    public List<Email> findByUserId(Long userId) {
        return emailMapper.findByUserId(userId);
    }

    /**
     * 根据发送方邮箱查找记录
     */
    public List<Email> findByEmail(String email) {
        return emailMapper.findByEmail(email);
    }

    /**
     * 根据接收方邮箱查找记录
     */
    public List<Email> findByToEmail(String toEmail) {
        return emailMapper.findByToEmail(toEmail);
    }

    /**
     * 更新邮箱记录
     */
    public Email updateEmail(Long id, EmailRequest request) {
        Email email = emailMapper.selectById(id);
        if (email == null) {
            throw new RuntimeException("邮箱记录不存在");
        }

        email.setUserId(request.getUserId());
        email.setEmail(request.getEmail());
        email.setToEmail(request.getToEmail());

        emailMapper.updateById(email);
        return email;
    }

    /**
     * 删除邮箱记录
     */
    public boolean deleteEmail(Long id) {
        Email email = emailMapper.selectById(id);
        if (email == null) {
            throw new RuntimeException("邮箱记录不存在");
        }

        return emailMapper.deleteById(id) > 0;
    }

    /**
     * 批量删除邮箱记录
     */
    public boolean deleteEmails(List<Long> ids) {
        return emailMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 根据用户ID删除所有邮箱记录
     */
    public boolean deleteByUserId(Long userId) {
        QueryWrapper<Email> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return emailMapper.delete(queryWrapper) > 0;
    }
}
