# RuralSync - 新农村协同治理平台

一个面向新农村地区的协同治理平台，包含后端 API 服务和微信小程序前端。

## 项目结构

```
NewCountrySys/
├── backend/          # Spring Boot 后端服务
│   ├── src/         # Java 源代码
│   ├── pom.xml      # Maven 配置文件
│   ├── sql/         # 数据库初始化脚本
│   └── README.md    # 后端说明文档
│
└── miniprogram/     # 微信小程序前端
    └── README.md    # 小程序说明文档
```

## 核心功能

1. **问题上报与跟踪**
   - 村民上报社区问题
   - 问题处理流程跟踪
   - 积分奖励机制

2. **社区互动**
   - 社区动态发布
   - 评论互动

3. **农产品市场**
   - 农产品信息发布
   - 在线交易

4. **用户管理**
   - 基于角色的权限控制
   - JWT 身份认证

## 技术栈

### 后端
- Spring Boot 2.x
- MyBatis-Plus
- Spring Security + JWT
- MySQL

### 前端
- 微信小程序原生开发

## 快速开始

### 后端服务
```bash
cd backend
mvn spring-boot:run
```

### 微信小程序
详见 `miniprogram/README.md`

## 开发状态

- ✅ 后端 API 开发完成
- 🚧 微信小程序开发中
