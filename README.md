# 后台管理系统

## 项目简介
本项目为基于 Spring Boot 和 Vue 的全栈开发示例，后端采用 Spring Boot 框架，前端采用 Vue 框架，适用于前后端分离的现代 Web 应用开发。

## 主要功能
- 用户管理（注册、登录、查询、修改、删除）
- 权限认证与安全控制
- 一对多待办事项（Todo）管理
- 聊天功能（集成DeepSeek对话接口）
- 文件上传与管理
- 数据展示与交互
- 基于 MyBatis-Plus 的数据库操作
- 跨域资源共享（CORS）配置
- 前后端分离架构

## 技术栈
- 后端：Spring Boot 3.x、MyBatis-Plus、MySQL、Hutool、Lombok
- 前端：Vue.js、Vue Router、Vuex、Axios
- 构建工具：Maven、npm

## 快速启动
### 后端启动
1. 进入 `springboot` 目录：
   ```bash
   cd springboot
   ```
2. 配置数据库连接（`src/main/resources/application.properties`），确保本地 MySQL 已创建 `springboot_vue` 数据库。
3. 使用 Maven 构建并启动：
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. 后端服务默认端口为 `9090`。

### 前端启动
1. 进入 `vue` 目录：
   ```bash
   cd vue
   ```
2. 安装依赖：
   ```bash
   npm install
   ```
3. 启动开发服务器：
   ```bash
   npm run serve
   ```
4. 前端服务默认端口为 `9876`。

## 目录结构说明
```
├── springboot           # 后端 Spring Boot 项目
│   ├── pom.xml
│   ├── src/
│   └── ...
├── vue                 # 前端 Vue 项目
│   ├── package.json
│   ├── src/
│   └── ...
├── LICENSE.txt         # 许可证
├── .gitignore
└── README.md           # 项目说明文档
```

## 其他说明
- 如需自定义数据库配置，请修改 `springboot/src/main/resources/application.properties`。
- 推荐使用 JDK 21 及以上版本。
- 如遇依赖问题，请检查 Maven 和 npm 源配置。

---
如有问题欢迎提交 Issue 或 PR。