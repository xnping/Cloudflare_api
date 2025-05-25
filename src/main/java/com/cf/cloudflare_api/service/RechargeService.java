package com.cf.cloudflare_api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.UseCardCodeRequest;
import com.cf.cloudflare_api.entity.CardCode;
import com.cf.cloudflare_api.entity.RechargeRecord;
import com.cf.cloudflare_api.entity.User;
import com.cf.cloudflare_api.mapper.CardCodeMapper;
import com.cf.cloudflare_api.mapper.RechargeRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RechargeService {
    
    private final CardCodeMapper cardCodeMapper;
    private final RechargeRecordMapper rechargeRecordMapper;
    private final UserService userService;
    
    public RechargeService(CardCodeMapper cardCodeMapper, RechargeRecordMapper rechargeRecordMapper, UserService userService) {
        this.cardCodeMapper = cardCodeMapper;
        this.rechargeRecordMapper = rechargeRecordMapper;
        this.userService = userService;
    }
    
    /**
     * 使用卡密充值
     */
    @Transactional
    public RechargeRecord useCardCode(Long userId, UseCardCodeRequest request) {
        // 查找卡密
        CardCode cardCode = cardCodeMapper.findByCode(request.getCode());
        if (cardCode == null) {
            throw new RuntimeException("卡密不存在");
        }
        
        // 检查卡密状态
        if (!"unused".equals(cardCode.getStatus())) {
            if ("used".equals(cardCode.getStatus())) {
                throw new RuntimeException("卡密已被使用");
            } else if ("disabled".equals(cardCode.getStatus())) {
                throw new RuntimeException("卡密已被禁用");
            }
        }
        
        // 检查是否过期
        if (cardCode.isExpired()) {
            throw new RuntimeException("卡密已过期");
        }
        
        // 获取用户信息
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        Integer beforeBalance = user.getFrequency();
        Integer afterBalance = beforeBalance + cardCode.getValue();
        
        // 更新用户余额
        user.setFrequency(afterBalance);
        userService.updateUser(userId, user);
        
        // 标记卡密为已使用
        cardCode.setStatus("used");
        cardCode.setUsedByUserId(userId);
        cardCode.setUsedAt(LocalDateTime.now());
        cardCodeMapper.updateById(cardCode);
        
        // 创建充值记录
        RechargeRecord record = new RechargeRecord(
            userId,
            request.getCode(),
            cardCode.getValue(),
            "card",
            beforeBalance,
            afterBalance,
            "卡密充值：" + cardCode.getDescription()
        );
        rechargeRecordMapper.insert(record);
        
        return record;
    }
    
    /**
     * 管理员充值
     */
    @Transactional
    public RechargeRecord adminRecharge(Long userId, Integer amount, String description) {
        // 获取用户信息
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        Integer beforeBalance = user.getFrequency();
        Integer afterBalance = beforeBalance + amount;
        
        // 更新用户余额
        user.setFrequency(afterBalance);
        userService.updateUser(userId, user);
        
        // 创建充值记录
        RechargeRecord record = new RechargeRecord(
            userId,
            null,
            amount,
            "admin",
            beforeBalance,
            afterBalance,
            description != null ? description : "管理员充值"
        );
        rechargeRecordMapper.insert(record);
        
        return record;
    }
    
    /**
     * 获取用户充值记录
     */
    public List<RechargeRecord> getUserRechargeRecords(Long userId) {
        return rechargeRecordMapper.findByUserId(userId);
    }
    
    /**
     * 获取所有充值记录
     */
    public List<RechargeRecord> getAllRechargeRecords() {
        return rechargeRecordMapper.selectList(null);
    }
    
    /**
     * 分页查询充值记录
     */
    public Page<RechargeRecord> findRechargeRecordsWithPagination(int pageNum, int pageSize) {
        Page<RechargeRecord> page = new Page<>(pageNum, pageSize);
        return rechargeRecordMapper.selectPage(page, null);
    }
    
    /**
     * 根据类型查询充值记录
     */
    public List<RechargeRecord> findByType(String type) {
        return rechargeRecordMapper.findByType(type);
    }
}
