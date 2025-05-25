-- 创建邮箱表（基础版本）
CREATE TABLE IF NOT EXISTS `email` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '邮箱记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `email` VARCHAR(255) NOT NULL COMMENT '发送方邮箱',
    `to_email` VARCHAR(255) NOT NULL COMMENT '接收方邮箱',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_email` (`email`),
    INDEX `idx_to_email` (`to_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮箱表';

-- 如果需要与用户表建立外键关系，可以添加以下约束（可选）
-- ALTER TABLE `email` ADD CONSTRAINT `fk_email_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 插入示例数据（可选）
INSERT INTO `email` (`user_id`, `email`, `to_email`) VALUES
(1, 'sender@example.com', 'receiver@example.com'),
(1, 'admin@example.com', 'user@example.com');
