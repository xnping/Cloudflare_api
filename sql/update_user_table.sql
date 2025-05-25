-- 更新用户表，添加时间字段
ALTER TABLE `user` 
ADD COLUMN `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
ADD COLUMN `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';

-- 为现有数据设置创建时间（如果有现有数据的话）
UPDATE `user` SET `created_at` = NOW(), `updated_at` = NOW() WHERE `created_at` IS NULL;
