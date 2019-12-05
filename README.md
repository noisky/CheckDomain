# CheckDomain
由于我看中了一个域名处于即将删除的状态，于是就诞生了 CheckDomain 来帮我监控这个域名。

CheckDomain 是一个用来检查域名是否能注册的 Api，基于 Spring Boot 构建，使用了阿里云域名查询 api；

可实现对域名状态查询，注册监控等，并在可以注册的时候发送邮件通知。

![邮件通知注册演示](https://img.ffis.me/images/2019/12/05/checkdomain.png)


### 接口调用：

- 请求方式：
  `Get请求`

- 查看域名状态：`/domain/xxx.com`

- 查看域名状态并在可注册时发送邮件通知：`/domain/xxx.com/querykey`

  - `querykey` 在 yml 配置文件中设置

可是使用第三方监控（如阿里云监控）来请求api来达到监控域名状态


### 已完成功能：
- 检查域名是否能注册
- 监控域名状态，如果可以注册，发送邮件通知
- 使用 freemarker 实现邮件模板的静态化
- 使用 logback 记录系统运行日志

### TODO：
- 限制接口的调用频率
- 统计接口的调用次数，调用耗时等信息
- 接入其他域名查询 api
