# XuanBlog Backend

<div align="center">
  
  [简体中文](./README.zh-CN.md) | English
  
  [![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=java)](https://www.oracle.com/java/)
  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-6DB33F?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
  [![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql)](https://www.mysql.com/)
  [![Redis](https://img.shields.io/badge/Redis-7.0-DC382D?style=flat-square&logo=redis)](https://redis.io/)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)
  
</div>

## 📖 项目简介

XuanBlog Backend 是一个基于 Spring Boot 3.5 构建的现代化博客系统后端服务。项目采用微服务架构设计，集成了博客管理、用户认证、图片上传、实时通信、智能记忆卡片等功能，旨在提供一个功能完善、性能优秀的个人博客解决方案。

### ✨ 核心特性

- **📝 完整的博客管理**：支持文章的增删改查、分类管理、标签系统、评论互动
- **🔐 安全的认证授权**：基于 Sa-Token 实现的灵活权限控制系统
- **🖼️ 多样化图片存储**：支持 GitHub CDN 和 MinIO 对象存储
- **💬 实时消息通知**：基于 WebSocket 的实时通信功能
- **🧠 智能记忆系统**：实现了 SM-17 算法的记忆卡片系统，支持科学复习
- **⚡ 高性能缓存**：Redis + Caffeine 双层缓存架构
- **📊 全面的监控**：接口日志记录、性能分析、异常追踪
- **🐳 容器化部署**：完整的 Docker Compose 部署方案

## 🛠️ 技术栈

### 后端框架
- **Spring Boot 3.5.0** - 主框架
- **Spring Web** - RESTful API
- **Spring WebSocket** - 实时通信
- **Spring AOP** - 切面编程

### 数据存储
- **MySQL 8.0** - 主数据库
- **Redis 7.0** - 缓存 & 会话存储
- **MyBatis-Plus 3.5.11** - ORM 框架

### 安全认证
- **Sa-Token 1.41.0** - 权限认证框架
- **JWT** - Token 管理

### 工具库
- **Knife4j 4.4.0** - API 文档
- **Hutool 5.8.26** - 工具集
- **Fastjson2** - JSON 处理
- **Caffeine** - 本地缓存
- **Redisson 3.45.1** - 分布式锁

### 存储服务
- **MinIO** - 对象存储
- **GitHub** - 图片 CDN

### 部署相关
- **Docker** - 容器化
- **Docker Compose** - 编排工具
- **Maven** - 构建工具

## 📁 项目结构

```
XuanBlog_Backend/
├── blog_admin/              # 管理端入口模块
│   └── src/
│       └── main/
│           ├── java/        # 主应用程序
│           └── resources/   # 配置文件
├── blog_common/             # 公共模块
│   └── src/
│       └── main/
│           └── java/
│               ├── config/  # 全局配置
│               ├── entity/  # 通用实体
│               ├── exception/ # 异常处理
│               └── handler/ # 处理器
├── blog_system/             # 系统核心模块
│   └── src/
│       └── main/
│           └── java/
│               ├── annotation/ # 自定义注解
│               ├── aop/      # 切面
│               ├── controller/ # 控制器
│               ├── domain/   # 领域模型
│               ├── mapper/   # 数据访问
│               └── service/  # 业务逻辑
├── blog_modules/            # 功能模块
│   ├── blog_card/          # 记忆卡片模块
│   ├── blog_message/       # 消息模块
│   └── blog_upload/        # 上传模块
├── sql/                     # 数据库脚本
├── docker-compose.yml       # Docker 编排文件
└── Dockerfile              # Docker 镜像定义
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- Docker & Docker Compose (可选)

### 本地开发

1. **克隆项目**
```bash
git clone https://github.com/yourusername/XuanBlog_Backend.git
cd XuanBlog_Backend
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p blog < sql/blog.sql
```

3. **修改配置**
```yaml
# 编辑 blog_admin/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
```

4. **启动项目**
```bash
mvn clean install
mvn spring-boot:run -pl blog_admin
```

### Docker 部署

1. **使用 Docker Compose 一键部署**
```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f xuanblog-app
```

2. **访问服务**
- 应用服务：http://localhost:8901/api
- API 文档：http://localhost:8901/api/doc.html
- MinIO 控制台：http://localhost:9001
- MySQL：localhost:3307
- Redis：localhost:6379

## 📡 API 文档

项目集成了 Knife4j，启动后访问 http://localhost:8901/api/doc.html 查看完整的 API 文档。

## 🏗️ 架构设计

### 系统架构

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   前端应用   │────▶│   Nginx    │────▶│ Spring Boot │
└─────────────┘     └─────────────┘     └─────────────┘
                                               │
                    ┌──────────────────────────┼──────────────────────────┐
                    │                          │                          │
                    ▼                          ▼                          ▼
            ┌─────────────┐           ┌─────────────┐           ┌─────────────┐
            │    MySQL    │           │    Redis    │           │   MinIO     │
            └─────────────┘           └─────────────┘           └─────────────┘
```

### 核心功能模块

#### 1. 博客管理模块
- 文章的 CRUD 操作
- 富文本编辑器支持
- Markdown 渲染
- 分类和标签管理
- 文章统计分析

#### 2. 用户认证模块
- 基于 Sa-Token 的认证
- 角色权限管理
- 会话管理
- 登录状态维持

#### 3. 缓存系统
- Redis 分布式缓存
- Caffeine 本地缓存
- 缓存注解支持
- 缓存预热和更新策略

#### 4. 记忆卡片系统
- **SM-17 算法实现**：基于三组件记忆模型（难度、稳定性、检索能力）
- **智能复习调度**：根据遗忘曲线自动计算最佳复习时间
- **学习数据分析**：提供详细的学习进度和效果分析
- **算法对比**：支持 SM-2 和 SM-17 算法切换和效果对比

#### 5. 消息通知系统
- WebSocket 实时通信
- 消息推送机制
- 在线用户管理
- 消息持久化

## 🔧 配置说明

### 应用配置

```yaml
# application.yml 主要配置项
server:
  port: 8901
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog
    username: root
    password: password
    
  redis:
    database: 0
    host: localhost
    port: 6379
    
sa-token:
  token-name: xuanBlog
  timeout: 2592000
  token-style: uuid
  
# 文件上传配置
blog:
  upload:
    type: github  # 可选 github 或 minio
    github:
      token: your_github_token
      owner: your_github_username
      repo: your_repo_name
    minio:
      endpoint: http://localhost:9000
      accessKey: minioadmin
      secretKey: minioadmin123
```

### 环境配置

项目支持多环境配置：
- `dev` - 开发环境（默认）
- `test` - 测试环境
- `prod` - 生产环境
- `docker` - Docker 环境

## 📈 性能优化

1. **数据库优化**
   - 合理的索引设计
   - 分页查询优化
   - 慢查询日志监控

2. **缓存策略**
   - 热点数据缓存
   - 缓存穿透防护
   - 缓存雪崩处理

3. **接口优化**
   - 接口限流
   - 请求合并
   - 异步处理

## 🔐 安全措施

- XSS 防护
- SQL 注入防护
- CSRF 防护
- 敏感数据加密
- 接口权限控制
- 请求频率限制

## 📝 开发规范

1. **代码规范**
   - 遵循阿里巴巴 Java 开发手册
   - 统一的代码格式化
   - 完善的注释文档

2. **Git 规范**
   - feat: 新功能
   - fix: 修复 bug
   - docs: 文档更新
   - style: 代码格式调整
   - refactor: 重构
   - test: 测试相关
   - chore: 构建过程或辅助工具的变动

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 作者

- **Ezhixuan** - *Initial work* - [GitHub](https://github.com/ezhixuan)

---

<div align="center">
  <p>如果这个项目对你有帮助，请给一个 ⭐️ Star！</p>
</div> 
