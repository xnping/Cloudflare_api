package com.cf.cloudflare_api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cf.cloudflare_api.dto.CreateCardCodeRequest;
import com.cf.cloudflare_api.entity.CardCode;
import com.cf.cloudflare_api.mapper.CardCodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CardCodeService {
    
    private final CardCodeMapper cardCodeMapper;
    
    public CardCodeService(CardCodeMapper cardCodeMapper) {
        this.cardCodeMapper = cardCodeMapper;
    }
    
    /**
     * 生成卡密
     */
    @Transactional
    public List<CardCode> generateCardCodes(CreateCardCodeRequest request) {
        List<CardCode> cardCodes = new ArrayList<>();
        
        for (int i = 0; i < request.getCount(); i++) {
            String code = generateUniqueCode();
            LocalDateTime expiresAt = null;
            
            if (request.getValidDays() != null && request.getValidDays() > 0) {
                expiresAt = LocalDateTime.now().plusDays(request.getValidDays());
            }
            
            CardCode cardCode = new CardCode(code, request.getValue(), expiresAt, request.getDescription());
            cardCodeMapper.insert(cardCode);
            cardCodes.add(cardCode);
        }
        
        return cardCodes;
    }
    
    /**
     * 生成唯一卡密码
     */
    private String generateUniqueCode() {
        String code;
        do {
            // 生成16位随机码
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        } while (cardCodeMapper.findByCode(code) != null);
        
        return code;
    }
    
    /**
     * 根据卡密码查找
     */
    public CardCode findByCode(String code) {
        return cardCodeMapper.findByCode(code);
    }
    
    /**
     * 获取所有卡密
     */
    public List<CardCode> findAllCardCodes() {
        return cardCodeMapper.selectList(null);
    }
    
    /**
     * 分页查询卡密
     */
    public Page<CardCode> findCardCodesWithPagination(int pageNum, int pageSize) {
        Page<CardCode> page = new Page<>(pageNum, pageSize);
        return cardCodeMapper.selectPage(page, null);
    }
    
    /**
     * 根据状态查找卡密
     */
    public List<CardCode> findByStatus(String status) {
        return cardCodeMapper.findByStatus(status);
    }
    
    /**
     * 禁用卡密
     */
    public CardCode disableCardCode(Long id) {
        CardCode cardCode = cardCodeMapper.selectById(id);
        if (cardCode == null) {
            throw new RuntimeException("卡密不存在");
        }
        
        if ("used".equals(cardCode.getStatus())) {
            throw new RuntimeException("卡密已被使用，无法禁用");
        }
        
        cardCode.setStatus("disabled");
        cardCodeMapper.updateById(cardCode);
        return cardCode;
    }
    
    /**
     * 启用卡密
     */
    public CardCode enableCardCode(Long id) {
        CardCode cardCode = cardCodeMapper.selectById(id);
        if (cardCode == null) {
            throw new RuntimeException("卡密不存在");
        }
        
        if ("used".equals(cardCode.getStatus())) {
            throw new RuntimeException("卡密已被使用，无法启用");
        }
        
        cardCode.setStatus("unused");
        cardCodeMapper.updateById(cardCode);
        return cardCode;
    }
    
    /**
     * 删除卡密
     */
    public boolean deleteCardCode(Long id) {
        CardCode cardCode = cardCodeMapper.selectById(id);
        if (cardCode == null) {
            throw new RuntimeException("卡密不存在");
        }
        
        return cardCodeMapper.deleteById(id) > 0;
    }
    
    /**
     * 批量删除卡密
     */
    public boolean deleteCardCodes(List<Long> ids) {
        return cardCodeMapper.deleteBatchIds(ids) > 0;
    }
    
    /**
     * 清理过期卡密
     */
    @Transactional
    public int cleanExpiredCards() {
        List<CardCode> expiredCards = cardCodeMapper.findExpiredUnusedCards();
        for (CardCode card : expiredCards) {
            card.setStatus("disabled");
            cardCodeMapper.updateById(card);
        }
        return expiredCards.size();
    }
}
