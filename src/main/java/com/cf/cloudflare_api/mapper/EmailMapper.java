package com.cf.cloudflare_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cf.cloudflare_api.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmailMapper extends BaseMapper<Email> {
    
    /**
     * 根据用户ID查找邮箱记录
     */
    @Select("SELECT * FROM email WHERE user_id = #{userId}")
    List<Email> findByUserId(@Param("userId") Long userId);
    
    /**
     * 根据发送方邮箱查找记录
     */
    @Select("SELECT * FROM email WHERE email = #{email}")
    List<Email> findByEmail(@Param("email") String email);
    
    /**
     * 根据接收方邮箱查找记录
     */
    @Select("SELECT * FROM email WHERE to_email = #{toEmail}")
    List<Email> findByToEmail(@Param("toEmail") String toEmail);
    
    /**
     * 根据用户ID和发送方邮箱查找记录
     */
    @Select("SELECT * FROM email WHERE user_id = #{userId} AND email = #{email}")
    List<Email> findByUserIdAndEmail(@Param("userId") Long userId, @Param("email") String email);
}
