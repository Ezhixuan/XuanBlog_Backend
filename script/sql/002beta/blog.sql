SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `account`     varchar(30) NOT NULL COMMENT '登录账号',
    `name`        varchar(30) NOT NULL COMMENT '用户昵称',
    `password`    char(60)    NOT NULL COMMENT 'bcrypt 密文',
    `email`       varchar(254)         DEFAULT NULL COMMENT '邮箱，RFC 5321 最大长度',
    `avatar`      varchar(255)         DEFAULT NULL COMMENT '头像 URL',
    `profile`     varchar(255)         DEFAULT NULL COMMENT '个人简介',
    `status`      tinyint(1)  NOT NULL DEFAULT '1' COMMENT '0-禁用 1-正常',
    `deleted`     tinyint(1)  NOT NULL DEFAULT '0' COMMENT '0-未删 1-已删',
    `role`        varchar(10) NOT NULL COMMENT '角色',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `account_password` (`account`, `password`) USING BTREE,
    KEY `idx_account` (`account`),
    KEY `idx_status_del` (`status`, `deleted`),
    KEY `idx_role` (`role`),
    CONSTRAINT `sys_user_chk_1` CHECK ((`status` in (0, 1))),
    CONSTRAINT `sys_user_chk_2` CHECK ((`deleted` in (0, 1)))
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户表';

-- ----------------------------
-- Table structure for sys_picture
-- ----------------------------
DROP TABLE IF EXISTS `sys_picture`;
CREATE TABLE `sys_picture`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `type`        tinyint(1)                                                    NOT NULL DEFAULT '1' COMMENT '图片类型 1.博客内容图片 2.博客封面图片 3.博客用户头像',
    `url`         varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片 url',
    `name`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片名称',
    `user_id`     bigint                                                        NOT NULL COMMENT '创建用户 id',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_url` (`url`) USING BTREE,
    KEY `idx_name` (`name`),
    KEY `idx_userId_updateTime` (`user_id`, `update_time` DESC) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='图片';

SET FOREIGN_KEY_CHECKS = 1;
