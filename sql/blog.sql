/*
 Navicat Premium Dump SQL

 Source Server         : docker_3307
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3307
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 27/03/2025 10:00:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL COMMENT '文章标题',
  `content` text NOT NULL COMMENT '文章内容',
  `summary` varchar(255) DEFAULT NULL COMMENT '文章摘要',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `status` tinyint DEFAULT '1' COMMENT '状态：1-已发布，0-草稿',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `article_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表';

-- ----------------------------
-- Records of article
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类表';

-- ----------------------------
-- Records of article_category
-- ----------------------------
BEGIN;
INSERT INTO `article_category` (`id`, `name`, `description`, `create_time`, `update_time`, `deleted`) VALUES (1, '技术', '技术相关文章', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_category` (`id`, `name`, `description`, `create_time`, `update_time`, `deleted`) VALUES (2, '生活', '生活随笔', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_category` (`id`, `name`, `description`, `create_time`, `update_time`, `deleted`) VALUES (3, '资讯', '行业资讯', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text NOT NULL COMMENT '评论内容',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `parent_id` bigint DEFAULT NULL COMMENT '父评论ID',
  `to_user_id` bigint DEFAULT NULL COMMENT '回复用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `article_id` (`article_id`),
  KEY `user_id` (`user_id`),
  KEY `parent_id` (`parent_id`),
  KEY `to_user_id` (`to_user_id`),
  CONSTRAINT `article_comment_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `article_comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `article_comment_ibfk_3` FOREIGN KEY (`parent_id`) REFERENCES `article_comment` (`id`),
  CONSTRAINT `article_comment_ibfk_4` FOREIGN KEY (`to_user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';

-- ----------------------------
-- Records of article_comment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for article_content
-- ----------------------------
DROP TABLE IF EXISTS `article_content`;
CREATE TABLE `article_content` (
  `article_id` bigint NOT NULL COMMENT '文章id',
  `content` text NOT NULL COMMENT '文章内容',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章内容';

-- ----------------------------
-- Records of article_content
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';

-- ----------------------------
-- Records of article_tag
-- ----------------------------
BEGIN;
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (1, 'Java', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (2, 'Spring Boot', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (3, 'Vue', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (4, 'TypeScript', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (5, 'MySQL', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (6, 'Redis', '2025-03-25 22:53:00', '2025-03-25 22:53:00', 0);
COMMIT;

-- ----------------------------
-- Table structure for article_tag_link
-- ----------------------------
DROP TABLE IF EXISTS `article_tag_link`;
CREATE TABLE `article_tag_link` (
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`article_id`) USING BTREE,
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `article_tag_link_ibfk_1` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `article_tag_link_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `article_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章标签关联表';

-- ----------------------------
-- Records of article_tag_link
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_interface_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_interface_log`;
CREATE TABLE `sys_interface_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `request_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `interface_name` varchar(255) NOT NULL COMMENT '接口名称',
  `request_params` text COMMENT '请求参数（JSON格式）',
  `response_data` text COMMENT '返回数据（JSON格式）',
  `status` enum('success','failure','timeout') NOT NULL DEFAULT 'success' COMMENT '调用状态',
  `error_info` text COMMENT '错误信息（失败时记录）',
  `execution_time` decimal(10,3) DEFAULT NULL COMMENT '执行耗时（秒）',
  `client_ip` varchar(45) DEFAULT NULL COMMENT '调用方IP地址',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（可选）',
  `request_id` varchar(64) DEFAULT NULL COMMENT '请求唯一标识（可选）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_interface_time` (`interface_name`,`request_time`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口调用日志表';

-- ----------------------------
-- Records of sys_interface_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_interface_log` (`id`, `request_time`, `interface_name`, `request_params`, `response_data`, `status`, `error_info`, `execution_time`, `client_ip`, `user_id`, `request_id`, `created_at`) VALUES (1, '2025-03-26 10:02:18', 'MainController.health()', '[]', '{\"code\":0,\"data\":\"ok\",\"message\":\"ok\"}', 'success', NULL, 0.000, '127.0.0.1', NULL, NULL, '2025-03-26 10:02:18');
INSERT INTO `sys_interface_log` (`id`, `request_time`, `interface_name`, `request_params`, `response_data`, `status`, `error_info`, `execution_time`, `client_ip`, `user_id`, `request_id`, `created_at`) VALUES (2, '2025-03-26 10:03:13', 'MainController.health()', '[]', '{\"code\":0,\"data\":\"ok\",\"message\":\"ok\"}', 'success', NULL, 1.000, '127.0.0.1', NULL, NULL, '2025-03-26 10:03:13');
INSERT INTO `sys_interface_log` (`id`, `request_time`, `interface_name`, `request_params`, `response_data`, `status`, `error_info`, `execution_time`, `client_ip`, `user_id`, `request_id`, `created_at`) VALUES (3, '2025-03-26 10:07:53', 'MainController.health()', '[]', '{\"code\":0,\"data\":\"ok\",\"message\":\"ok\"}', 'success', NULL, 1773.000, '127.0.0.1', NULL, NULL, '2025-03-26 10:08:02');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `user_account` varchar(255) NOT NULL COMMENT '用户账号',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `profile` varchar(255) DEFAULT NULL COMMENT '个人简介',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  `role` varchar(255) NOT NULL COMMENT '用户角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `password`, `user_account`, `username`, `avatar`, `profile`, `email`, `status`, `create_time`, `update_time`, `deleted`, `role`) VALUES (1, '0e039d030b9ba451310bb48d213ddd69', 'ezhixuan', 'ezhixuan', NULL, NULL, NULL, 1, '2025-03-26 23:14:10', '2025-03-26 23:14:10', 0, 'admin');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
