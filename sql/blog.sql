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

 Date: 02/04/2025 00:22:34
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
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `summary` varchar(255) DEFAULT NULL COMMENT '文章摘要',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `tag_ids` varchar(255) DEFAULT NULL COMMENT '标签集合',
  `word_count` int DEFAULT '0' COMMENT '文章字数',
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
) ENGINE=InnoDB AUTO_INCREMENT=1907104873345118211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章表';

-- ----------------------------
-- Records of article
-- ----------------------------
BEGIN;
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153711497781250, '1', 2, '', '', 1, '1', 0, 1, 0, 0, 0, '2025-03-27 15:03:35', '2025-03-30 18:43:48', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153781316165633, '1', 2, '', '', 1, '1', 0, 1, 0, 0, 0, '2025-03-27 15:03:52', '2025-03-30 22:05:44', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153784034074625, '1', 2, '', '', 1, '1', 0, 1, 0, 0, 0, '2025-03-27 15:03:52', '2025-03-30 18:43:53', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153786403856386, '1', 2, '', '', 1, '1', 0, 0, 0, 0, 0, '2025-03-27 15:03:53', '2025-03-27 15:03:53', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153790182924290, '1', 2, '', '', 1, '1', 0, 2, 0, 0, 0, '2025-03-27 15:03:54', '2025-03-30 18:48:08', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153793274126337, '1', 2, '', '', 1, '1', 0, 0, 0, 0, 0, '2025-03-27 15:03:55', '2025-03-27 15:03:55', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153795589382146, '1', 2, '', '', 1, '1', 0, 0, 0, 0, 0, '2025-03-27 15:03:55', '2025-03-27 15:03:55', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153798210822145, '1', 2, '', '', 1, '1', 0, 0, 0, 0, 0, '2025-03-27 15:03:56', '2025-03-27 15:03:56', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153800744181762, '1', 2, '', '', 1, '1', 0, 0, 0, 0, 0, '2025-03-27 15:03:56', '2025-03-27 15:03:56', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153813280956417, '11', 2, '', '', 1, '1', 0, 5, 0, 0, 0, '2025-03-27 15:03:59', '2025-03-30 18:50:01', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153822453899266, '111', 2, '', '', 1, '1', 0, 1, 0, 0, 0, '2025-03-27 15:04:01', '2025-03-30 02:51:06', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905153831618453506, '1121', 2, '', '', 1, '1', 0, 2, 0, 0, 0, '2025-03-27 15:04:04', '2025-03-30 18:43:29', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905260592695255041, '1121', 2, '', '', 1, '1', 0, 1, 0, 0, 0, '2025-03-27 22:08:17', '2025-03-30 02:51:01', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905260778725220353, '1121', 2, '想干些啥呢', 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/1899093818518831106/2025-03-17_1d1f5804-4ecc-4c73-bc0c-3f84e1047bc8.jpg', 1, '5', 0, 6, 0, 0, 0, '2025-03-27 22:09:02', '2025-03-30 22:05:37', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905655033339719681, '阿斯顿', 2, NULL, NULL, 1, '1', 0, 5, 0, 0, 1, '2025-03-29 00:15:39', '2025-03-30 04:56:48', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905658854954643457, '21312', 2, '1231', NULL, 1, '1,6', 0, 4, 0, 0, 1, '2025-03-29 00:30:51', '2025-03-30 05:29:35', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905686617031942145, '也是好起来啦', 2, '测试文章 记录面试准备', NULL, 1, '1', 0, 7, 0, 0, 1, '2025-03-29 02:21:10', '2025-03-30 06:37:21', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905845117561270273, 'wwww', 2, 'asdjla', NULL, 2, '1,2', 0, 10, 0, 0, 1, '2025-03-29 12:50:59', '2025-03-30 22:05:27', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905845801350262785, '我是测试提交', 2, '这是一片策划师啊是建档立卡', NULL, 3, '5,6', 0, 39, 0, 0, 1, '2025-03-29 12:53:42', '2025-03-30 18:50:28', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1905976812813873154, '测试博客', 2, '测试博客内容代码块高亮', NULL, 3, '2,4', 0, 72, 0, 0, 1, '2025-03-29 21:34:18', '2025-03-30 23:17:50', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1906343919065993218, '测试', 2, '111', NULL, 2, '3,7', 0, 3, 0, 0, 1, '2025-03-30 21:53:03', '2025-03-30 23:31:18', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1906347325209939970, '测试', 2, '我是一个测试', NULL, 2, '6', 0, 9, 0, 0, 1, '2025-03-30 22:06:35', '2025-04-01 17:52:17', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1906355317137444866, '1', 2, '1', NULL, 2, '3', 0, 3, 0, 0, 1, '2025-03-30 22:38:20', '2025-03-31 12:33:30', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1906367260464222210, '21321', 2, '213', 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic/images202503311124102.jpg', 2, '3', 0, 10, 0, 0, 1, '2025-03-30 23:25:48', '2025-04-01 15:01:36', 0);
INSERT INTO `article` (`id`, `title`, `user_id`, `summary`, `cover`, `category_id`, `tag_ids`, `word_count`, `view_count`, `like_count`, `comment_count`, `status`, `create_time`, `update_time`, `deleted`) VALUES (1907104873345118210, '0401-测试图片上传', 2, '测试图片上传', 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/2/2025-04-02_0b6f0a53-4580-42b5-b4a3-c5a8ce9407c3.jpg', 2, '2', 0, 2, 0, 0, 1, '2025-04-02 00:16:48', '2025-04-02 00:17:09', 0);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类表';

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
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905260592695255041, '11122233还不错吧');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905260778725220353, '11122233还不错吧');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905655033339719681, '瓦达');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905658854954643457, '# 21312\n\n## 引言\n在这篇博客中，我们将探讨21312相关的内容。\n\n## 主要内容\n21312是一个非常有趣的话题，它涉及到许多方面的知识。\n\n### 关键点1\n这是关于21312的第一个关键点。\n\n### 关键点2\n这是关于21312的第二个关键点。\n\n## 总结\n通过本文的介绍，我们对21312有了更深入的了解。希望这篇文章对你有所帮助！\n\n---\n*这是由AI自动生成的内容简述，请根据实际需要修改完善。*');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905686617031942145, '# Java七天高效学习计划（面试专用）\n\n## 📋 总体规划\n\n- **目标受众**：具有Java基础，准备面试的开发者\n- **每日时长**：4-6小时，分成上午和下午两个学习时段\n- **学习方法**：理论学习(40%) + 实践编码(40%) + 面试题演练(20%)\n- **计划特点**：重点掌握面试高频考点，结合当下互联网企业的技术需求\n\n## 🚀 详细计划\n\n### Day 1：Java基础与Java 8+新特性\n\n#### 上午：Java基础回顾\n\n- **核心内容**：\n  - 面向对象三大特性（封装、继承、多态）深度理解\n  - 接口与抽象类的本质区别及使用场景\n  - 泛型与类型擦除机制\n  - 异常体系及最佳实践\n\n#### 下午：Java 8+函数式编程\n\n- **核心内容**：\n  - Lambda表达式原理及使用场景\n  - 函数式接口（Functional Interface）设计与应用\n  - Stream API核心操作（filter/map/reduce等）\n  - Optional类避免NPE的实战技巧\n  - Java 9-17的关键新特性速览（模块化、记录类等）\n\n#### 实战任务：\n\n- 重构传统代码为函数式编程风格\n- 使用Stream API处理大数据集合（排序、过滤、统计）\n\n#### 面试重点：\n\n- Lambda表达式的底层实现原理？\n- Stream的惰性计算与终止操作如何协同工作？\n- 为什么要用Optional而非null判断？\n\n---\n\n### Day 2：集合框架与并发容器\n\n#### 上午：集合框架深度剖析\n\n- **核心内容**：\n  - HashMap工作原理与演进（JDK7到JDK8的变化）\n  - HashMap/LinkedHashMap/TreeMap对比与应用场景\n  - ArrayList/LinkedList底层实现及性能对比\n  - HashSet/TreeSet特性与内部实现\n\n#### 下午：并发容器与实践\n\n- **核心内容**：\n  - ConcurrentHashMap原理（JDK7分段锁vs JDK8 CAS+Synchronized）\n  - CopyOnWriteArrayList/Set使用场景\n  - 并发队列类型（ArrayBlockingQueue, LinkedBlockingQueue, ConcurrentLinkedQueue）\n  - 集合框架线程安全解决方案对比\n\n#### 实战任务：\n\n- 手写简易LRU缓存（使用LinkedHashMap）\n- 实现高性能线程安全的计数器（比较不同并发容器的性能）\n\n#### 面试重点：\n\n- HashMap为什么线程不安全？具体表现是什么？\n- ConcurrentHashMap如何保证高并发下的线程安全？\n- 为什么HashMap的容量是2的幂次方？\n\n---\n\n### Day 3：多线程与并发编程\n\n#### 上午：线程基础与线程池\n\n- **核心内容**：\n  - 线程的生命周期与状态转换\n  - ThreadLocal原理与内存泄漏问题\n  - 线程池核心参数与工作流程\n  - 四种常见线程池与使用场景\n\n#### 下午：并发控制与锁机制\n\n- **核心内容**：\n  - synchronized原理与优化（锁升级过程）\n  - ReentrantLock与AQS（AbstractQueuedSynchronizer）框架\n  - volatile关键字与内存屏障\n  - 原子类与CAS操作原理\n  - CompletableFuture异步编程实战\n\n#### 实战任务：\n\n- 实现生产者-消费者模型（分别用wait/notify和Condition实现）\n- 使用CompletableFuture优化多任务并行处理\n\n#### 面试重点：\n\n- synchronized和ReentrantLock的区别与选择？\n- volatile如何保证可见性但不保证原子性？\n- 什么是AQS，为什么它是并发编程的核心？\n\n---\n\n### Day 4：JVM原理与性能调优\n\n#### 上午：JVM内存模型\n\n- **核心内容**：\n  - JVM内存区域划分（堆、栈、方法区等）\n  - 对象创建流程与内存分配策略\n  - 垃圾回收算法与分代回收\n  - 常见垃圾收集器对比（Serial、Parallel、CMS、G1、ZGC）\n\n#### 下午：性能监控与调优\n\n- **核心内容**：\n  - JVM调优参数详解\n  - 常见OOM问题分析与解决\n  - JVM性能监控工具（JVisualVM, Arthas, MAT）\n  - GC日志分析与调优实践\n\n#### 实战任务：\n\n- 模拟内存泄漏场景并使用工具诊断\n- JVM参数调优实践（以Web应用为例）\n\n#### 面试重点：\n\n- 如何判断对象是否可以被回收？\n- G1收集器的优势和工作原理？\n- 遇到过OOM问题吗？如何定位和解决？\n\n---\n\n### Day 5：Spring生态与微服务\n\n#### 上午：Spring核心原理\n\n- **核心内容**：\n  - Spring IoC容器工作原理\n  - Spring Bean生命周期详解\n  - Spring AOP实现原理（动态代理）\n  - Spring事务管理机制\n\n#### 下午：Spring Boot与微服务\n\n- **核心内容**：\n  - Spring Boot自动配置原理\n  - Spring Boot常用注解与最佳实践\n  - Spring Cloud核心组件（Eureka/Nacos, Feign, Gateway, Config）\n  - 微服务设计模式与实践\n\n#### 实战任务：\n\n- 手写简易IoC容器（理解Spring核心原理）\n- 搭建简单的Spring Boot微服务Demo\n\n#### 面试重点：\n\n- Spring如何解决循环依赖问题？\n- @Transactional注解失效的场景有哪些？\n- 微服务架构的优缺点和适用场景？\n\n---\n\n### Day 6：数据库与分布式\n\n#### 上午：MySQL与ORM框架\n\n- **核心内容**：\n  - MySQL索引原理（B+树结构）\n  - 事务ACID特性与隔离级别\n  - SQL优化策略与执行计划分析\n  - MyBatis/JPA核心原理对比\n\n#### 下午：分布式技术\n\n- **核心内容**：\n  - 分布式事务解决方案（2PC、TCC、SAGA）\n  - 分布式锁实现（基于Redis/Zookeeper）\n  - 消息队列应用（Kafka/RabbitMQ）\n  - Redis核心数据结构与缓存策略\n\n#### 实战任务：\n\n- 设计并实现分布式锁（Redis实现）\n- 编写高性能SQL并进行执行计划分析\n\n#### 面试重点：\n\n- MySQL如何实现MVCC？\n- Redis缓存穿透、击穿、雪崩的区别和解决方案？\n- 如何保证消息队列的可靠性传输？\n\n---\n\n### Day 7：系统设计与面试模拟\n\n#### 上午：系统设计\n\n- **核心内容**：\n  - 高并发系统设计原则\n  - 限流、熔断、降级实现方案\n  - 大型系统架构演进（单体→微服务→云原生）\n  - 性能优化方法论\n\n#### 下午：面试模拟与总结\n\n- **核心内容**：\n  - 高频算法题解析（链表、树、动态规划）\n  - 项目经验提炼与表达\n  - 常见面试场景模拟\n  - 整理知识体系，查漏补缺\n\n#### 实战任务：\n\n- 设计高并发秒杀系统方案\n- 模拟面试并进行复盘\n\n#### 面试重点：\n\n- 如何设计一个高并发的系统？\n- 从技术选型到架构设计的思考过程？\n- 项目中遇到的最大挑战及解决方案？\n\n## 📚 推荐学习资源\n\n1. **书籍**：\n   - 《Java并发编程实战》\n   - 《深入理解Java虚拟机》\n   - 《Spring技术内幕》\n   - 《高性能MySQL》\n\n2. **网站与视频**：\n   - 尚硅谷Java高级教程（B站）\n   - Java官方文档（Oracle）\n   - JavaGuide开源项目（GitHub）\n   - 力扣（LeetCode）Java题解\n\n3. **工具**：\n   - JDK 17/11\n   - IntelliJ IDEA\n   - Arthas（阿里开源的Java诊断工具）\n   - JMeter（性能测试）\n   - MAT（内存分析工具）\n\n## 💡 学习建议\n\n1. **主动编码**：每个知识点都要亲手实现，单元测试验证\n2. **源码阅读**：核心类的源码必须掌握（如HashMap、ConcurrentHashMap）\n3. **精简笔记**：用思维导图记录知识点关联，便于记忆\n4. **模拟面试**：找同学互相提问，或自己录制回答视频\n5. **项目实战**：将学到的知识应用到实际项目中\n\n## 🎯 面试准备策略\n\n1. 准备一份简历技术亮点清单，确保能深入解释每一项\n2. 对项目经历按STAR法则（情境-任务-行动-结果）进行梳理\n3. 准备3-5个技术难点解决案例，体现解决问题的思路\n4. 对每个知识点，不仅要知道\"是什么\"，还要理解\"为什么\"和\"怎么用\"\n\n记住：面试不仅是考验知识广度，更是测试思考深度和学习能力。保持自信，展示学习热情和解决问题的能力！');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905845117561270273, '我说说哈哈哈哈啊的山山水水');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905845801350262785, '力扣 240820 每日一题\n题目链接：[3154. 到达第 K 级台阶的方案数](https://leetcode.cn/problems/student-attendance-record-ii/description/?envType=daily-question&envId=2024-08-19)\n\n 简单\n 中等\n 困难\n使用语言：Java\n关键思路：记忆化搜索\n给你有一个 非负 整数 k 。有一个无限长度的台阶，最低 一层编号为 0 。\n\nAlice 有一个整数 jump ，一开始值为 0 。Alice 从台阶 1 开始，可以使用 任意 次操作，目标是到达第 k 级台阶。假设 Alice 位于台阶 i ，一次 操作 中，Alice 可以：\n\n向下走一级到 i - 1 ，但该操作 不能 连续使用，如果在台阶第 0 级也不能使用。\n向上走到台阶 i + 2jump 处，然后 jump 变为 jump + 1 。\n请你返回 Alice 到达台阶 k 处的总方案数。\n\n注意，Alice 可能到达台阶 k 处后，通过一些操作重新回到台阶 k 处，这视为不同的方案。\n\n示例 1：\n\n**输入：**k = 0\n\n**输出：**2\n\n解释：\n\n2 种到达台阶 0 的方案为：\n\nAlice 从台阶 1 开始。\n执行第一种操作，从台阶 1 向下走到台阶 0 。\nAlice 从台阶 1 开始。\n执行第一种操作，从台阶 1 向下走到台阶 0 。\n执行第二种操作，向上走 20 级台阶到台阶 1 。\n执行第一种操作，从台阶 1 向下走到台阶 0 。\n示例 2：\n\n**输入：**k = 1\n\n**输出：**4\n\n解释：\n\n4 种到达台阶 1 的方案为：\n\nAlice 从台阶 1 开始，已经到达台阶 1 。\nAlice 从台阶 1 开始。\n执行第一种操作，从台阶 1 向下走到台阶 0 。\n执行第二种操作，向上走 20 级台阶到台阶 1 。\nAlice 从台阶 1 开始。\n执行第二种操作，向上走 20 级台阶到台阶 2 。\n执行第一种操作，向下走 1 级台阶到台阶 1 。\nAlice 从台阶 1 开始。\n执行第一种操作，从台阶 1 向下走到台阶 0 。\n\n执行第二种操作，向上走 20 级台阶到台阶 1 。\n\n执行第一种操作，向下走 1 级台阶到台阶 0 。\n\n执行第二种操作，向上走 21 级台阶到台阶 2 。\n\n执行第一种操作，向下走 1 级台阶到台阶 1 。\n\n提示：\n\n0 <= k <= 109\n题解\n```java\nclass Solution {\n    Map<String, Integer> memo = new HashMap<>();\n    public int waysToReachStair(int k) {\n        return dfs(k,1,0, true);\n    }\n    public int dfs(int target, int curr, int cntUp, boolean down){\n        if (curr > target + 1) return 0;\n        String key = curr + \",\" + cntUp + \",\" + down;\n        if (memo.containsKey(key)) {\n            return memo.get(key);\n        }\n        int up = (int)Math.pow(2, cntUp);\n        System.out.println(\"cntUp = \" + cntUp + \" down = \" + down + \" up = \" + up + \" curr = \" + curr);\n        int res = curr == target ? 1 : 0;\n        if (curr > 0 && down){\n            res += dfs(target, curr - 1, cntUp, false);\n        }\n\n        res += dfs(target, curr + up, cntUp + 1, true);\n        System.out.println(\"res = \" + res);\n        memo.put(key, res);\n        return res;\n    }\n}\n```\n为了解决这个问题，我们使用了深度优先搜索（DFS）结合记忆化的方法。记忆化帮助我们存储已经计算过的特定状态的结果，避免重复计算，从而显著提高效率。\n\n关键实现步骤：\n递归函数 (dfs):\n\n\ndfs\n函数的参数包括：\n\ntarget：目标台阶k。\ncurr：当前所在的台阶。\ncntUp：记录了从开始到现在执行向上跳的次数。\ndown：一个布尔值，表示上一步是否是向下跳，用于确保向下跳不会连续执行。\n每次调用dfs时，都会根据当前台阶位置curr，cntUp次数和上一步是否向下跳的情况生成一个唯一的键值，以此作为记忆化的索引。\n\n递归逻辑：\n\n如果curr超过了target + 1，直接返回0，因为这意味着Alice走得太远了。\n如果当前状态已经在memo中有记录，直接返回该状态的结果。\n根据当前位置curr是否等于target来初始化结果res。\n如果上一步不是向下走且curr > 0，尝试向下走一步。\n尝试向上跳到curr + 2^cntUp，并增加跳的次数。\n将当前状态和计算结果存入memo中。\n终止条件：\n\n如果curr恰好等于target，记录为一种到达方式。\n代码分析\n代码使用记忆化以优化递归过程，避免了无效的重复计算。这种方法确保了每种状态只计算一次，并且可以快速从记忆中获取结果。');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1905976812813873154, '```java\n    @PostMapping(\"/category/add\")\n    public BaseResponse<ArticleCategoryVO> submitCategory(@RequestBody String categoryName) {\n        ArticleCategory articleCategory = new ArticleCategory();\n        ArticleCategory category = this.categoryService.getOne(Wrappers.<ArticleCategory>lambdaQuery().eq(ArticleCategory::getName, categoryName));\n        if (Objects.isNull(category)) {\n            articleCategory.setName(categoryName);\n            this.categoryService.save(articleCategory);\n        }\n        ArticleCategoryVO articleCategoryVO = BeanUtil.copyProperties(articleCategory, ArticleCategoryVO.class);\n        return R.success(articleCategoryVO);\n    }\n```');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1906343919065993218, '我是输入文档测试功能\n\n## 二级标题\n\n# 一级标题\n\n哈哈哈\n完熟哦sada \n\n\n```java\n    public Object doInterceptor(ProceedingJoinPoint joinPoint, Cache cache) {\n        Object[] args = joinPoint.getArgs();\n        String name = joinPoint.getSignature().getName();\n        String key = DigestUtils.md5DigestAsHex(JSON.toJSONString(args).getBytes());\n        key = preKey + name + \":\" + key;\n        String resTypeName = ((MethodSignature) joinPoint.getSignature()).getReturnType().getName();\n        Class<?> aClass = Class.forName(resTypeName);\n        Object o = LOCAL_CACHE.getIfPresent(key);\n\n        if (Objects.nonNull(o)) {\n            if (aClass.equals(BaseResponse.class)) {\n                return R.success(o);\n            }\n            return aClass.cast(o);\n        } else {\n            o = redisUtil.get(key);\n        }\n        if (Objects.nonNull(o)) {\n            if (aClass.equals(BaseResponse.class)) {\n                return R.success(o);\n            }\n            return o;\n        }\n```\n\n总说i 哈说');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1906347325209939970, '# 测试\n\n## 引言\n在这篇博客中，我们将探讨测试相关的内容。\n\n## 主要内容\n测试是一个非常有趣的话题，它涉及到许多方面的知识。\n\n### 关键点1\n这是关于测试的第一个关键点。\n\n### 关键点2\n这是关于测试的第二个关键点。\n\n## 总结\n通过本文的介绍，我们对测试有了更深入的了解。希望这篇文章对你有所帮助！\n\n---\n*这是由AI自动生成的内容简述，请根据实际需要修改完善。*');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1906355317137444866, '# 1\n\n## 引言\n在这篇博客中，我们将探讨1相关的内容。\n\n## 主要内容\n1是一个非常有趣的话题，它涉及到许多方面的知识。\n\n### 关键点1\n这是关于1的第一个关键点。\n\n### 关键点2\n这是关于1的第二个关键点。\n\n## 总结\n通过本文的介绍，我们对1有了更深入的了解。希望这篇文章对你有所帮助！\n\n---\n*这是由AI自动生成的内容简述，请根据实际需要修改完善。*');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1906367260464222210, '# 21321\n\n## 引言\n在这篇博客中，我们将探讨21321相关的内容。\n\n## 主要内容\n21321是一个非常有趣的话题，它涉及到许多方面的知识。\n\n### 关键点1\n这是关于21321的第一个关键点。\n\n### 关键点2\n这是关于21321的第二个关键点。\n\n## 总结\n通过本文的介绍，我们对21321有了更深入的了解。希望这篇文章对你有所帮助！\n\n---\n*这是由AI自动生成的内容简述，请根据实际需要修改完善。*');
INSERT INTO `article_content` (`article_id`, `content`) VALUES (1907104873345118210, '# 0401-测试图片上传\n\n## 引言\n在这篇博客中，我们将探讨0401-测试图片上传相关的内容。\n\n## 主要内容\n0401-测试图片上传是一个非常有趣的话题，它涉及到许多方面的知识。\n\n### 关键点1\n这是关于0401-测试图片上传的第一个关键点。\n\n### 关键点2\n这是关于0401-测试图片上传的第二个关键点。\n\n## 总结\n通过本文的介绍，我们对0401-测试图片上传有了更深入的了解。希望这篇文章对你有所帮助！\n\n---\n*这是由AI自动生成的内容简述，请根据实际需要修改完善。*');
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';

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
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (7, '\"测试\"', '2025-03-30 21:52:02', '2025-03-30 21:52:02', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (8, '\"收到\"', '2025-03-30 21:52:20', '2025-03-30 21:52:20', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (9, '\"测试2\"', '2025-03-30 21:54:00', '2025-03-30 21:54:00', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (10, '\"springboot\"', '2025-04-01 13:46:31', '2025-04-01 13:46:31', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (11, '{\"name\":\"测试\"}', '2025-04-01 13:52:36', '2025-04-01 13:52:36', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (12, '{\"name\":\"哈哈哈\"}', '2025-04-01 13:55:09', '2025-04-01 13:55:09', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (13, '{\"name\":\"我是谁\"}', '2025-04-01 13:57:17', '2025-04-01 13:57:17', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (14, 'da', '2025-04-01 14:25:51', '2025-04-01 14:25:51', 0);
INSERT INTO `article_tag` (`id`, `name`, `create_time`, `update_time`, `deleted`) VALUES (15, 'asd', '2025-04-01 14:26:17', '2025-04-01 14:26:17', 0);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口调用日志表';

-- ----------------------------
-- Records of sys_interface_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_picture
-- ----------------------------
DROP TABLE IF EXISTS `sys_picture`;
CREATE TABLE `sys_picture` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片 url',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片名称',
  `user_id` bigint NOT NULL COMMENT '创建用户 id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_userId` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1902618593193299975 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图片';

-- ----------------------------
-- Records of sys_picture
-- ----------------------------
BEGIN;
INSERT INTO `sys_picture` (`id`, `url`, `name`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES (1902618593193299971, 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/1/2025-04-01_b1f579ef-73d6-4157-85eb-c5e2df830691.jpg', '2025-04-01_b1f579ef-73d6-4157-85eb-c5e2df830691.jpg', 1, '2025-04-01 22:38:49', '2025-04-01 22:38:49', 0);
INSERT INTO `sys_picture` (`id`, `url`, `name`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES (1902618593193299972, 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/1/2025-04-02_e32aef6b-6dd1-4a6c-83a8-5330e045ea91.jpg', '2025-04-02_e32aef6b-6dd1-4a6c-83a8-5330e045ea91.jpg', 1, '2025-04-02 00:11:22', '2025-04-02 00:11:22', 0);
INSERT INTO `sys_picture` (`id`, `url`, `name`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES (1902618593193299973, 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/1/2025-04-02_7ab376b1-44cd-4a87-833d-d0337b73806c.jpg', '2025-04-02_7ab376b1-44cd-4a87-833d-d0337b73806c.jpg', 1, '2025-04-02 00:11:44', '2025-04-02 00:11:44', 0);
INSERT INTO `sys_picture` (`id`, `url`, `name`, `user_id`, `create_time`, `update_time`, `deleted`) VALUES (1902618593193299974, 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/2/2025-04-02_0b6f0a53-4580-42b5-b4a3-c5a8ce9407c3.jpg', '2025-04-02_0b6f0a53-4580-42b5-b4a3-c5a8ce9407c3.jpg', 2, '2025-04-02 00:16:27', '2025-04-02 00:16:27', 0);
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=1905622609184583682 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `password`, `user_account`, `username`, `avatar`, `profile`, `email`, `status`, `create_time`, `update_time`, `deleted`, `role`) VALUES (2, '39d84ed11c2763a8844cdc5c302b3143', 'ezhixuan', 'ezhixuan', 'https://cdn.jsdelivr.net/gh/Ezhixuan/myPic@main/public/1899093818518831106/2025-03-16_2e97de3b-cf8d-455f-b462-0fad9afbecc4.jpg', NULL, NULL, 1, '2025-04-01 22:43:41', '2025-03-26 23:14:10', 0, 'admin');
INSERT INTO `sys_user` (`id`, `password`, `user_account`, `username`, `avatar`, `profile`, `email`, `status`, `create_time`, `update_time`, `deleted`, `role`) VALUES (1905622609184583681, '39d84ed11c2763a8844cdc5c302b3143', 'ezhixuanna', 'ezhixuanna', NULL, NULL, NULL, 1, '2025-04-01 22:43:32', '2025-03-28 22:06:49', 0, 'user');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
