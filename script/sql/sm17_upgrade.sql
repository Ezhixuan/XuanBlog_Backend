-- SM-17算法升级脚本
-- 为memo_card表添加SM-17算法所需的新字段

DELIMITER //

-- 创建存储过程来安全添加列
CREATE PROCEDURE AddColumnIfNotExists()
BEGIN
    -- 添加difficulty列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'difficulty'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN difficulty DECIMAL(5,4) DEFAULT 0.5 COMMENT '项目难度(0-1范围，0最容易，1最困难)';
    END IF;

    -- 添加stability列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'stability'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN stability DECIMAL(10,2) DEFAULT 4.0 COMMENT '记忆稳定性(天数，表示在90%检索成功率下可维持的时间)';
    END IF;

    -- 添加retrievability列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'retrievability'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN retrievability DECIMAL(5,4) DEFAULT 0.9 COMMENT '当前检索能力(0-1范围，表示当前时刻成功回忆的概率)';
    END IF;

    -- 添加last_review_time列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'last_review_time'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN last_review_time DATETIME DEFAULT NULL COMMENT '上次复习时间(用于计算时间间隔和检索能力衰减)';
    END IF;

    -- 添加algorithm_version列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'algorithm_version'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN algorithm_version VARCHAR(10) DEFAULT 'SM17' COMMENT '算法版本标识(SM2/SM17)';
    END IF;

    -- 添加lapse_count列
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'memo_card' 
        AND COLUMN_NAME = 'lapse_count'
    ) THEN
        ALTER TABLE memo_card 
        ADD COLUMN lapse_count BIGINT DEFAULT 0 COMMENT '记忆失败次数(用于失败后的间隔计算)';
    END IF;

END //

DELIMITER ;

-- 执行存储过程
CALL AddColumnIfNotExists();

-- 删除存储过程
DROP PROCEDURE AddColumnIfNotExists;

-- 为新字段添加索引（使用安全的索引创建方式）
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'memo_card' 
     AND INDEX_NAME = 'idx_memo_card_algorithm_version') = 0,
    'CREATE INDEX idx_memo_card_algorithm_version ON memo_card(algorithm_version)',
    'SELECT ''Index idx_memo_card_algorithm_version already exists'''
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'memo_card' 
     AND INDEX_NAME = 'idx_memo_card_last_review_time') = 0,
    'CREATE INDEX idx_memo_card_last_review_time ON memo_card(last_review_time)',
    'SELECT ''Index idx_memo_card_last_review_time already exists'''
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'memo_card' 
     AND INDEX_NAME = 'idx_memo_card_difficulty') = 0,
    'CREATE INDEX idx_memo_card_difficulty ON memo_card(difficulty)',
    'SELECT ''Index idx_memo_card_difficulty already exists'''
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'memo_card' 
     AND INDEX_NAME = 'idx_memo_card_stability') = 0,
    'CREATE INDEX idx_memo_card_stability ON memo_card(stability)',
    'SELECT ''Index idx_memo_card_stability already exists'''
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 为现有数据初始化SM-17字段
-- 注意：这个更新操作应该在低峰期执行，避免锁表时间过长

UPDATE memo_card 
SET 
    -- 根据易度因子估算初始难度
    difficulty = CASE 
        WHEN ease_factor IS NOT NULL THEN 
            GREATEST(0.0, LEAST(1.0, (4.0 - ease_factor) / 2.5))
        ELSE 0.5 
    END,
    
    -- 根据复习间隔估算初始稳定性
    stability = CASE 
        WHEN review_interval IS NOT NULL AND review_interval > 0 THEN 
            GREATEST(1.0, LEAST(90.0, review_interval / (24.0 * 60.0) * 2))
        ELSE 4.0 
    END,
    
    -- 根据最近质量分数估算检索能力
    retrievability = CASE 
        WHEN quality IS NOT NULL THEN
            CASE quality
                WHEN 0 THEN 0.0
                WHEN 1 THEN 0.0  
                WHEN 2 THEN 0.6
                WHEN 3 THEN 0.8
                WHEN 4 THEN 0.95
                WHEN 5 THEN 1.0
                ELSE 0.9
            END
        ELSE 0.9
    END,
    
    -- 设置上次复习时间
    last_review_time = COALESCE(update_time, create_time, NOW() - INTERVAL 1 DAY),
    
    -- 设置算法版本
    algorithm_version = 'SM17'

WHERE 
    -- 只更新还未设置SM-17字段的记录
    (difficulty IS NULL OR stability IS NULL OR retrievability IS NULL 
     OR last_review_time IS NULL OR algorithm_version IS NULL);

-- 创建视图便于分析SM-17算法效果
CREATE OR REPLACE VIEW v_memo_card_sm17_stats AS
SELECT 
    algorithm_version,
    COUNT(*) as total_cards,
    AVG(difficulty) as avg_difficulty,
    AVG(stability) as avg_stability,
    AVG(retrievability) as avg_retrievability,
    AVG(review_interval) as avg_interval_minutes,
    AVG(repetitions) as avg_repetitions,
    AVG(lapse_count) as avg_lapses,
    COUNT(CASE WHEN quality >= 3 THEN 1 END) / COUNT(*) * 100 as success_rate
FROM memo_card 
WHERE algorithm_version IS NOT NULL
GROUP BY algorithm_version;

-- 创建性能监控视图
CREATE OR REPLACE VIEW v_memo_card_performance AS
SELECT 
    DATE(update_time) as review_date,
    algorithm_version,
    COUNT(*) as reviews_count,
    AVG(quality) as avg_quality,
    COUNT(CASE WHEN quality >= 3 THEN 1 END) / COUNT(*) * 100 as success_rate,
    AVG(CASE WHEN algorithm_version = 'SM17' THEN difficulty END) as avg_difficulty,
    AVG(CASE WHEN algorithm_version = 'SM17' THEN stability END) as avg_stability
FROM memo_card 
WHERE update_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
    AND algorithm_version IS NOT NULL
GROUP BY DATE(update_time), algorithm_version
ORDER BY review_date DESC;

-- 创建changelog表（如果不存在）
CREATE TABLE IF NOT EXISTS memo_card_changelog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT 'SM算法版本变更日志';

-- 插入说明注释
INSERT INTO memo_card_changelog (version, description, created_at) 
VALUES ('SM17_v1.0', 'SM-17算法升级：添加三组件记忆模型支持(DSR)', NOW())
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- 输出升级完成信息
SELECT 'SM-17算法升级完成！' AS message;
SELECT COUNT(*) AS updated_cards FROM memo_card WHERE algorithm_version = 'SM17';

COMMIT; 