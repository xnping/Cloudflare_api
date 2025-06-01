# Spring Boot API Service

这是一个基于 Spring Boot + MyBatis-Plus 的 RESTful API 服务项目，从 Flask 重构而来。

## 项目设置

1. 确保已安装 Java 17+ 和 Maven

2. 配置 MySQL 数据库：
   - 确保已安装并启动 MySQL 服务
   - 创建数据库：
   ```sql
   CREATE DATABASE boot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   - 在 `application.properties` 中配置数据库连接信息

3. 运行服务：
```bash
mvn spring-boot:run
```
或者
```bash
./mvnw spring-boot:run
```

服务将在 http://localhost:5000 启动

## API 端点

所有 API 响应格式如下：
```json
{
  "code": 200,      // 状态码
  "message": "...", // 消息说明
  "data": {}        // 数据（可选）
}
```

### 用户认证与授权接口

#### 注册
- **URL**: `/api/register`
- **方法**: `POST`
- **权限**: 无需认证，任何人都可以注册，默认注册为普通用户，除非特别指定
- **请求参数**:
  ```json
  {
    "username": "用户名",
    "password": "密码",
    "email": "邮箱地址",
    "permissions": "user"  // 可选，值为 'user'或'admin'，默认为'user'
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "user": {
        "id": 1,
        "username": "用户名",
        "email": "邮箱地址",
        "frequency": 0,
        "permissions": "user"
      },
      "token": "JWT令牌"
    }
  }
  ```

#### 登录
- **URL**: `/api/login`
- **方法**: `POST`
- **权限**: 无需认证，任何用户（无论是管理员还是普通用户）只要有有效账号都可以登录
- **请求参数**:
  ```json
  {
    "username": "用户名或邮箱",
    "password": "密码"
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "user": {
        "id": 1,
        "username": "用户名",
        "email": "邮箱地址",
        "frequency": 0,
        "permissions": "user"
      },
      "token": "JWT令牌"
    }
  }
  ```

#### 获取当前用户信息
- **URL**: `/api/user/info`
- **方法**: `GET`
- **权限**: 需要认证
- **请求头**:
  ```
  Authorization: Bearer <token>
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "email": "邮箱地址",
      "frequency": 0,
      "permissions": "user"
    }
  }
  ```

### 管理员接口

#### 修改用户权限
- **URL**: `/api/user/permissions`
- **方法**: `PUT`
- **权限**: 仅管理员
- **请求头**:
  ```
  Authorization: Bearer <token>
  ```
- **请求参数**:
  ```json
  {
    "user_id": 1,
    "permissions": "admin"  // 'user'或'admin'
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "用户权限更新成功",
    "data": {
      "id": 1,
      "username": "用户名",
      "email": "邮箱地址",
      "frequency": 0,
      "permissions": "admin"
    }
  }
  ```

#### 获取所有用户列表
- **URL**: `/api/users`
- **方法**: `GET`
- **权限**: 仅管理员
- **请求头**:
  ```
  Authorization: Bearer <token>
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "获取用户列表成功",
    "data": [
      {
        "id": 1,
        "username": "用户名1",
        "email": "邮箱地址1",
        "frequency": 0,
        "permissions": "admin"
      },
      {
        "id": 2,
        "username": "用户名2",
        "email": "邮箱地址2",
        "frequency": 0,
        "permissions": "user"
      }
    ]
  }
  ```

### 系统接口

#### 健康检查
- **URL**: `/api/health`
- **方法**: `GET`
- **权限**: 无需认证
- **响应**:
  ```json
  {
    "code": 200,
    "message": "API服务正常运行"
  }
  ```

## 错误码说明

- 200: 请求成功
- 400: 请求参数错误
- 401: 身份验证失败
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

## 权限说明

系统有两种用户权限：
- `user`: 普通用户，可以访问基本功能
- `admin`: 管理员，可以管理所有用户及其权限

## 技术栈

- **框架**: Spring Boot 3.5.0
- **数据库**: MySQL
- **ORM**: MyBatis-Plus 3.5.5
- **安全**: Spring Security + JWT
- **构建工具**: Maven
- **Java版本**: 17+

## 开发说明

- 使用 MySQL 数据库
- 使用 MyBatis-Plus 作为 ORM 框架
- 已启用 CORS 支持跨域请求
- 所有API响应均为 JSON 格式
- 用户认证使用 JWT Token 机制
- 密码使用 BCrypt 加密