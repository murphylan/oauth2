#!/bin/bash

# API请求
RESPONSE=$(curl -s -X POST "http://localhost:9000/oauth2/token" \
  -u 'messaging-client:secret' \
  -d 'grant_type=client_credentials' \
  -d 'scope=message.read message.write')

# 解析JSON获取access_token  
# ACCESS_TOKEN=$(echo $RESPONSE | jq -r '.access_token')
# sed解析
ACCESS_TOKEN=$(echo $RESPONSE | sed -n 's/.*"access_token":"\([^"]*\)".*/\1/p')

echo "Access Token: $ACCESS_TOKEN"