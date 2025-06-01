#!/bin/bash

# 安装MySQL 5.7脚本 - CentOS 7
# 作者：Augment
# 功能：从零安装MySQL 5.7，设置root密码为123456，并允许远程连接

# 显示彩色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # 无颜色

echo -e "${GREEN}开始安装MySQL 5.7...${NC}"

# 1. 添加MySQL Yum仓库
echo -e "${GREEN}添加MySQL官方Yum仓库...${NC}"
rpm -Uvh https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm

# 导入MySQL GPG密钥
echo -e "${GREEN}导入MySQL GPG密钥...${NC}"
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022

# 清理yum缓存
echo -e "${GREEN}清理YUM缓存...${NC}"
yum clean all
yum makecache

# 2. 安装MySQL服务器和客户端
echo -e "${GREEN}安装MySQL服务器和客户端...${NC}"
yum -y install mysql-community-server mysql-community-client --nogpgcheck

# 3. 启动MySQL服务
echo -e "${GREEN}启动MySQL服务...${NC}"
systemctl start mysqld
systemctl enable mysqld

# 4. 获取临时密码
echo -e "${GREEN}获取MySQL临时root密码...${NC}"
temp_password=$(grep 'temporary password' /var/log/mysqld.log | awk '{print $NF}')
echo -e "临时密码是: ${RED}$temp_password${NC}"

# 5. 配置MySQL
echo -e "${GREEN}配置MySQL...${NC}"
cat > /tmp/mysql_secure_installation.sql << EOF
-- 修改root密码
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
-- 允许root远程登录
CREATE USER 'root'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
-- 删除匿名用户
DELETE FROM mysql.user WHERE User='';
-- 禁止root远程登录
-- 已在上面允许root远程登录，所以注释掉这行
-- DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');
-- 删除测试数据库
DROP DATABASE IF EXISTS test;
DELETE FROM mysql.db WHERE Db='test' OR Db='test\\_%';
-- 刷新权限
FLUSH PRIVILEGES;
EOF

# 使用临时密码执行安全配置脚本
echo -e "${GREEN}执行MySQL安全配置...${NC}"
mysql -uroot -p"$temp_password" --connect-expired-password < /tmp/mysql_secure_installation.sql

# 检查是否成功
if [ $? -eq 0 ]; then
    echo -e "${GREEN}MySQL安全配置成功完成！${NC}"
else
    echo -e "${RED}MySQL安全配置失败，请检查错误信息。${NC}"
    exit 1
fi

# 6. 配置MySQL以允许远程连接
echo -e "${GREEN}配置MySQL允许远程连接...${NC}"
cat > /etc/my.cnf.d/mysql-server.cnf << EOF
[mysqld]
bind-address = 0.0.0.0
EOF

# 7. 重启MySQL服务
echo -e "${GREEN}重启MySQL服务...${NC}"
systemctl restart mysqld

# 8. 配置防火墙
echo -e "${GREEN}配置防火墙允许MySQL端口(3306)...${NC}"
firewall-cmd --permanent --add-port=3306/tcp
firewall-cmd --reload

# 9. 验证安装
echo -e "${GREEN}验证MySQL安装...${NC}"
mysql -uroot -p123456 -e "SELECT VERSION();"

# 10. 清理
rm -f /tmp/mysql_secure_installation.sql

echo -e "${GREEN}MySQL 5.7 安装完成！${NC}"
echo -e "${GREEN}用户名: root${NC}"
echo -e "${GREEN}密码: 123456${NC}"
echo -e "${GREEN}MySQL已配置为允许远程连接${NC}"
echo -e "${GREEN}MySQL服务端口: 3306${NC}"