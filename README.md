CheckDomain
========
![license]
![GitHub repo size]


由于我看中了一个域名处于即将删除的状态，于是就诞生了 CheckDomain 来帮我监控这个域名。

CheckDomain 是一个用来检查域名是否能注册的 Api，基于 Spring Boot 构建，使用了阿里云域名查询 Api；

可实现对域名状态查询，可注册监控，whois信息查询等，并在域名可以注册的时候发送邮件通知。

![邮件通知注册演示](https://img.ffis.me/images/2019/12/05/checkdomain.png)


### 接口调用：

1、查看域名状态：

- 请求方式：`GET请求`
    
    - 请求地址：`/domain/{name}`
    
    - 参数说明：
        
        - `{name}`： 查询的域名

2、查看域名状态并在可注册时发送邮件通知：

1）请求方式：`GET请求`

- 请求地址：`/domain/{name}/{email}/{querykey}`

- 参数说明：
 
    - `{name}`： 查询的域名
    
    - `{email}`： 接受通知的邮箱地址
   
    - `querykey`： 查询密码，在 yml 配置文件中设置
        
2）请求方式：`POST请求`

- 请求地址：`/domain/{name}`

- 参数说明：
 
    - `{name}`： 查询的域名
    
    - POST提交的表单：
    
        - `email`： 接受通知的邮箱地址
        
        - `querykey`： 查询密码，在 yml 配置文件中设置

> 可以使用第三方监控（如阿里云监控）来请求 Api 来达到监控域名注册状态

3、查询域名whois信息：

- 请求方式：`GET请求`
    
    - 请求地址：`/whois/{domain}`
    
    - 参数说明：
        
        - `{domain}`： 查询的域名
        
        - 暂时只支持 .com/.net/.edu/.org/.me/.cn 域名的查询

### 已完成功能：
- 检查域名是否能注册
- 监控域名状态，如果可以注册，发送邮件通知
- 使用 Freemarker 实现邮件模板的静态化
- 使用 Logback 记录系统运行日志
- 实现域名的 whois 信息查询（参考了 tammypi 的 whoisutil 工具类）

### TODO：
- 编写前端页面，方便查询
- 限制接口的调用频率
- 统计并记录接口的调用次数，调用耗时等信息
- 接入其他域名查询 Api
- ...

### Thanks：
tammypi：[whoisutil][whoisutil]

[license]:https://img.shields.io/github/license/noisky/CheckDomain?color=blue
[GitHub repo size]:https://img.shields.io/github/repo-size/noisky/CheckDomain?logo=git
[whoisutil]:https://github.com/tammypi/whoisutil
