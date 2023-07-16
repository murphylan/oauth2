#!/bin/bash

# 授权服务器链接
AUTH_SERVER="http://localhost:9000"

# 客户端信息 
CLIENT_ID="messaging-client"

# 回调地址
REDIRECT_URI="http://127.0.0.1:8080/authorized"

SCOPES="openid%20profile%20message.read%20message.write"

# 构建授权链接
AUTH_URL="${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&scope=${SCOPES}"

# 使用curl请求授权链接
curl -L -sS "$AUTH_URL"  

curl -L -X GET "$AUTH_URL"

# 从重定向url中解析授权码
REDIRECTED_URL=`curl -LsS -D - "$AUTH_URL" | grep Location | awk '{print $2}'`
AUTH_CODE=`echo $REDIRECTED_URL | cut -d= -f2 | cut -d& -f1`

echo "Auth Code: $AUTH_CODE"