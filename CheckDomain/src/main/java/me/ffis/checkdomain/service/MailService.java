package me.ffis.checkdomain.service;

import me.ffis.checkdomain.model.MailTemplateModel;

/**
 * 邮件发送服务接口
 * Created by fanfan on 2019/12/05.
 */
public interface MailService {
    //发送邮件
    public void sendSimpleMail(MailTemplateModel model);

    //邮件模板静态化
    public String getMailHtml(MailTemplateModel mailTemplateModel);
}
