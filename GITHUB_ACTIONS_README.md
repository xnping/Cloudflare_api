# GitHub Actions 自动构建说明

## 概述

本项目配置了GitHub Actions自动构建，当你推送代码到GitHub时会自动构建和打包JAR文件。

## 工作流文件

### 1. `.github/workflows/build.yml` - 完整构建流程
- **触发条件**: 推送到 main/master 分支或创建 Pull Request
- **功能**: 
  - 运行测试
  - 构建JAR包
  - 上传构建产物
  - 创建GitHub Release（仅主分支）

### 2. `.github/workflows/simple-build.yml` - 简化构建流程
- **触发条件**: 推送到 main/master 分支或创建 Pull Request  
- **功能**:
  - 运行测试
  - 构建JAR包
  - 上传构建产物

## 如何使用

### 第一次设置

1. **提交工作流文件到GitHub**:
   ```bash
   git add .github/
   git commit -m "添加GitHub Actions自动构建"
   git push origin main
   ```

2. **查看构建状态**:
   - 访问你的GitHub仓库
   - 点击 "Actions" 标签页
   - 查看构建进度和结果

### 获取构建的JAR包

#### 方法1: 从Actions页面下载
1. 进入GitHub仓库的 "Actions" 页面
2. 点击最新的构建任务
3. 在 "Artifacts" 部分下载JAR包

#### 方法2: 从Releases页面下载（仅完整构建流程）
1. 进入GitHub仓库的 "Releases" 页面
2. 下载最新版本的JAR包

## 构建产物说明

- **JAR包位置**: `target/CloudFlare_Api-0.0.1-SNAPSHOT.jar`
- **保存时间**: 30天
- **命名规则**: `cloudflare-api-{构建编号}`

## 本地测试构建

在推送到GitHub之前，你可以本地测试构建：

```bash
# 清理并编译
./mvnw clean compile

# 运行测试
./mvnw test

# 打包JAR
./mvnw package
```

## 故障排除

### 常见问题

1. **权限错误**: 确保 `mvnw` 有执行权限
2. **Java版本**: 确保使用Java 17
3. **依赖问题**: 检查网络连接和Maven仓库访问

### 查看构建日志

1. 进入GitHub Actions页面
2. 点击失败的构建任务
3. 展开相应的步骤查看详细日志

## 自定义配置

### 修改触发条件

编辑 `.github/workflows/build.yml` 中的 `on` 部分：

```yaml
on:
  push:
    branches: [ main, develop ]  # 添加其他分支
  pull_request:
    branches: [ main ]
```

### 修改Java版本

编辑工作流文件中的Java设置：

```yaml
- name: 设置 Java
  uses: actions/setup-java@v4
  with:
    java-version: '21'  # 改为其他版本
    distribution: 'temurin'
```

## 部署建议

构建完成后，你可以：

1. **手动部署**: 下载JAR包到服务器运行
2. **自动部署**: 添加部署步骤到工作流
3. **Docker部署**: 结合Dockerfile自动构建镜像

## 注意事项

- 确保项目中的测试能够通过
- 大型项目构建可能需要更多时间
- GitHub Actions有使用限制，注意配额
- 私有仓库的Actions使用时间有限制
