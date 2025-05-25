-- CloudFlare API Database Initialization Script
-- 创建用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `email` VARCHAR(120) UNIQUE NOT NULL COMMENT '邮箱',
    `frequency` INT DEFAULT 0 COMMENT '使用频次',
    `permissions` VARCHAR(20) DEFAULT 'user' COMMENT '权限：user/admin',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_permissions` (`permissions`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建邮箱表
CREATE TABLE IF NOT EXISTS `email` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '邮箱记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `email` VARCHAR(255) NOT NULL COMMENT '发送方邮箱',
    `to_email` VARCHAR(255) NOT NULL COMMENT '接收方邮箱',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_email` (`email`),
    INDEX `idx_to_email` (`to_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮箱表';

-- 创建卡密表
CREATE TABLE IF NOT EXISTS `card_code` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '卡密ID',
    `code` VARCHAR(32) UNIQUE NOT NULL COMMENT '卡密码',
    `value` INT NOT NULL COMMENT '面值（充值的frequency数量）',
    `status` VARCHAR(20) DEFAULT 'unused' COMMENT '状态：unused-未使用，used-已使用，disabled-已禁用',
    `used_by_user_id` BIGINT DEFAULT NULL COMMENT '使用者用户ID',
    `used_at` DATETIME DEFAULT NULL COMMENT '使用时间',
    `expires_at` DATETIME DEFAULT NULL COMMENT '过期时间，NULL表示永久有效',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '卡密描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_code` (`code`),
    INDEX `idx_status` (`status`),
    INDEX `idx_used_by_user_id` (`used_by_user_id`),
    INDEX `idx_expires_at` (`expires_at`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='卡密表';

-- 创建充值记录表
CREATE TABLE IF NOT EXISTS `recharge_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '充值记录ID',
    `user_id` BIGINT NOT NULL COMMENT '充值用户ID',
    `card_code` VARCHAR(32) DEFAULT NULL COMMENT '使用的卡密',
    `amount` INT NOT NULL COMMENT '充值数量',
    `type` VARCHAR(20) NOT NULL COMMENT '充值类型：card-卡密充值，admin-管理员充值',
    `before_balance` INT NOT NULL COMMENT '充值前余额',
    `after_balance` INT NOT NULL COMMENT '充值后余额',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '充值描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_card_code` (`card_code`),
    INDEX `idx_type` (`type`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值记录表';

-- 插入示例管理员用户（密码是 admin123，已用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `email`, `frequency`, `permissions`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM1JA.dqP9nXkuP7O.S6', 'admin@example.com', 1000, 'admin')
ON DUPLICATE KEY UPDATE username=username;

-- 插入示例卡密
INSERT INTO `card_code` (`code`, `value`, `status`, `description`) VALUES
('DEMO1234ABCD5678', 10, 'unused', '演示卡密-10次'),
('TEST9876EFGH5432', 50, 'unused', '演示卡密-50次'),
('SAMPLE1111IJKL2222', 100, 'unused', '演示卡密-100次')
ON DUPLICATE KEY UPDATE code=code;
