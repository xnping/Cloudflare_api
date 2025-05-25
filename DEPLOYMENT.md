# CloudFlare API 部署指南

## 🚀 Railway 部署步骤

### 1. 准备工作
- GitHub 账户
- Railway 账户 (https://railway.app)

### 2. 部署步骤

#### 步骤1：登录Railway
1. 访问 https://railway.app
2. 使用GitHub账户登录

#### 步骤2：创建新项目
1. 点击 "New Project"
2. 选择 "Deploy from GitHub repo"
3. 选择 `xnping/Cloudflare_api` 仓库

#### 步骤3：添加MySQL数据库
1. 在项目中点击 "Add Service"
2. 选择 "Database" → "MySQL"
3. Railway会自动创建MySQL实例

#### 步骤4：配置环境变量
在Railway项目设置中添加以下环境变量：

```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-super-secret-jwt-key-here
CORS_ORIGINS=https://your-frontend-domain.com
```

#### 步骤5：数据库初始化
1. 连接到Railway MySQL数据库
2. 执行 `database-init.sql` 脚本

#### 步骤6：部署
Railway会自动检测到Spring Boot项目并开始部署。

### 3. 获取部署URL
部署完成后，Railway会提供一个公共URL，类似：
`https://your-app-name.railway.app`

### 4. 测试部署
访问：`https://your-app-name.railway.app/api/health`

## 🔧 环境变量说明

| 变量名 | 说明 | 示例值 |
|--------|------|--------|
| `DATABASE_URL` | 数据库连接URL | Railway自动提供 |
| `DATABASE_USERNAME` | 数据库用户名 | Railway自动提供 |
| `DATABASE_PASSWORD` | 数据库密码 | Railway自动提供 |
| `JWT_SECRET` | JWT密钥 | 至少32位随机字符串 |
| `CORS_ORIGINS` | 允许的前端域名 | https://your-frontend.com |
| `PORT` | 服务端口 | Railway自动提供 |

## 📝 默认管理员账户

部署完成后，可以使用以下账户登录：
- **用户名**: admin
- **密码**: admin123
- **邮箱**: admin@example.com

## 🔗 API文档

部署完成后，完整的API文档可以在项目根目录的 `Complete_API_Documentation.md` 中查看。

## 🆘 故障排除

### 常见问题
1. **数据库连接失败**: 检查环境变量是否正确设置
2. **应用启动失败**: 查看Railway日志，通常是依赖问题
3. **CORS错误**: 检查CORS_ORIGINS环境变量

### 查看日志
在Railway控制台中可以查看实时日志来诊断问题。
