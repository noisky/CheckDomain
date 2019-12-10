package me.ffis.checkdomain.service;

import me.ffis.checkdomain.model.MailTemplateModel;

/**
 * 邮件发送服务接口
 * Created by fanfan on 2019/12/05.
 */
public interface MailService {
    /**
     * 发送邮件服务
     *
     * @param model 发送邮件的数据模型
     * @param email 接受邮件的邮箱
     */
    public void sendSimpleMail(MailTemplateModel model, String email);

    /**
     * 邮件模板静态化
     *
     * @param mailTemplateModel 模板数据模型
     * @return 加上数据后的静态化模板
     */
    public String getMailHtml(MailTemplateModel mailTemplateModel);
}
