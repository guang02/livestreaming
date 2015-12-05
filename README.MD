# 云游戏直播平台Response Api简易说明文档

---
# 响应JSON

- ## code
 描述调用的api返回结果的初略描述。

  | 代码   | 描述       |
  |:------:|:----------:|
  |200     |返回结果正确 |
  |500     |一般性错误|
  |501     |post数据错误|
  |505     |流服务器错误    |


- ## data
 返回数据的载体。

- ## message
 返回自然语言的提示信息。

 ---

# 接口Url

 - 使用jquery异步传输数据。传输数据方法为post。
 - *根url：172.18.219.135:8080/livestreaming/*
 - 目前链接要使用相对路径，方便以后修改。

- ## 注册
 - *url：./user/register*
 - 参见*url：./user*

- ## 登录
 - *url：./user/login*
 - 参见*url：./*

- ## 注册房间
 - *url：./room/registerRoom*
 - 参见*url：./room*

- ## 保存录像
 - *url：./room/liveRoomToRecordRoom?id=12*
 - id从*url：room/liveRoom*获取

- ## 获取游戏类型列表
 - *url：./gameType*
