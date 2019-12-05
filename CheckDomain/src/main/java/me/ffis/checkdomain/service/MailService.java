package me.ffis.checkdomain.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import me.ffis.checkdomain.model.MailTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;

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
