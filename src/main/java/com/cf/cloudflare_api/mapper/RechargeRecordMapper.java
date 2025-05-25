package com.cf.cloudflare_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cf.cloudflare_api.entity.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RechargeRecordMapper extends BaseMapper<RechargeRecord> {
    
    /**
     * 根据用户ID查找充值记录
     */
    @Select("SELECT * FROM recharge_record WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<RechargeRecord> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据卡密查找充值记录
     */
    @Select("SELECT * FROM recharge_record WHERE card_code = #{cardCode}")
    RechargeRecord findByCardCode(@Param("cardCode") String cardCode);
    
    /**
     * 根据充值类型查找记录
     */
    @Select("SELECT * FROM recharge_record WHERE type = #{type} ORDER BY created_at DESC")
    List<RechargeRecord> findByType(@Param("type") String type);
}
