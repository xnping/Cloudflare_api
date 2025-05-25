package com.cf.cloudflare_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cf.cloudflare_api.entity.CardCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CardCodeMapper extends BaseMapper<CardCode> {
    
    /**
     * 根据卡密码查找
     */
    @Select("SELECT * FROM card_code WHERE code = #{code}")
    CardCode findByCode(@Param("code") String code);
    
    /**
     * 根据状态查找卡密
     */
    @Select("SELECT * FROM card_code WHERE status = #{status}")
    List<CardCode> findByStatus(@Param("status") String status);
    
    /**
     * 查找已过期但状态仍为unused的卡密
     */
    @Select("SELECT * FROM card_code WHERE status = 'unused' AND expires_at IS NOT NULL AND expires_at < NOW()")
    List<CardCode> findExpiredUnusedCards();
    
    /**
     * 根据使用者查找卡密
     */
    @Select("SELECT * FROM card_code WHERE used_by_user_id = #{userId}")
    List<CardCode> findByUsedByUserId(@Param("userId") Long userId);
}
