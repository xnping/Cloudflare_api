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

-- 如果需要与用户表建立外键关系，可以添加以下约束（可选）
-- ALTER TABLE `card_code` ADD CONSTRAINT `fk_card_code_user_id` FOREIGN KEY (`used_by_user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
-- ALTER TABLE `recharge_record` ADD CONSTRAINT `fk_recharge_record_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- 插入示例数据（可选）
INSERT INTO `card_code` (`code`, `value`, `status`, `description`) VALUES
('ABCD1234EFGH5678', 10, 'unused', '10次充值卡'),
('IJKL9012MNOP3456', 50, 'unused', '50次充值卡'),
('QRST7890UVWX1234', 100, 'unused', '100次充值卡');

-- 插入示例充值记录（可选）
-- INSERT INTO `recharge_record` (`user_id`, `card_code`, `amount`, `type`, `before_balance`, `after_balance`, `description`) VALUES
-- (1, 'ABCD1234EFGH5678', 10, 'card', 0, 10, '卡密充值：10次充值卡'),
-- (1, NULL, 20, 'admin', 10, 30, '管理员充值');
