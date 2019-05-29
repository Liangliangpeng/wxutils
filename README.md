# 微信生态开发常用接口使用案例
#### 本项目是一个微信生态开发常用接口的案例集合，其中包括：解密小程序code获取openid，发送小程序模板消息，发送公众号模板消息，用户关注、取关公众号的事件处理等。

#### 扩展知识  
openid:一个小程序的每个用户都有一个唯一的openid，是一个小程序用户中用户的唯一身份标志，公众号也一样;  
unionid:用户的小程序、公众号可在微信开放平台进行关联，关联之后的小程序和公众号有一个相同的unionid，公众号始终有unionid，小程序如果没有和公众号关联是
没有unionid的。

### 1.0：微信小程序解密code得到openid([官方文档](https://developers.weixin.qq.com/miniprogram/dev/api/wx.login.html))
过程如下：  
1.前端传入code到后端;  
2.后端解密code得到openid、session_key、unionid(小程序和公众号关联后才能解密出unionid)。

### 1.1：发送小程序模板消息([官方文档](https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/template-message.html))
过程如下：  
必备知识：发送模板消息到指定用户需要用户的formid和openid，formid是用户进行表单提交时微信生成的一个序列号，有效期为一周，开发者每收集一个用户的
formid即可以向该用户发送一次模板消息。  
1.收集用户的formid；  
2.向指定用户发送模板消息。

### 1.2：对微信公众号进行服务器配置校验token([参考博客](https://blog.csdn.net/LONG_Yi_1994/article/details/90022307))
过程如下：  
如果进行微信公众号开发，进行服务器配置是必要的，通过配置，用户与公众号的事件交互消息将发送至开发者配置的URL上，开发者可在自己写的接口内进行事件处理。  
1.填写配置信息，确保URL可访问；  
2.开发token验证接口。

### 1.3：用户与公众号事件交互处理(包括用户关注、取关公众号事件)
过程如下：  
1：用户关注或取关公众号后，微信将把事件消息推送至开发者填写的URL上，开发者可在接口内进行处理，例如：保存用户openid，依据openid得到unionid等。

### 1.4：发送公众号模板消息([官方文档](https://mp.weixin.qq.com/advanced/tmplmsg?action=faq&token=611048160&lang=zh_CN))
过程如下：  
1：发送公众号模板消息和发送小程序模板消息类似，只是发送公众号模板消息是没有限制的，有用户的openid即可发送。
