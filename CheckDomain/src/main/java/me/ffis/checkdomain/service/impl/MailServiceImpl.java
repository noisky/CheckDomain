package me.ffis.checkdomain.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import me.ffis.checkdomain.model.LogFileName;
import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.service.MailService;
import me.ffis.checkdomain.util.LoggerUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 邮件发送服务
 * Created by fanfan on 2019/12/05.
 */

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private final Logger maillogger = LoggerUtils.Logger(LogFileName.MAIL_LOGS);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${customize.mail.sender}")
    private String MAILSENDER;

    @Value("${customize.mail.receive}")
    private String MAILRECEIVER;

    //发送邮件
    public void sendSimpleMail(MailTemplateModel model) {
        try {
            //获取MimeMessage对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            if (MAILSENDER == null) {
                log.error("邮件收信人不能为空");
                throw new RuntimeException("邮件收信人不能为空");
            }
            String senderName = "域名注册监控Api";
            try {
                senderName = MimeUtility.encodeText(senderName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                log.error("编码异常", e);
            }
            //邮件发送人
            helper.setFrom(new InternetAddress(senderName + " <" + MAILSENDER + ">"));
            //邮件接收人
            helper.setTo(MAILRECEIVER);
            //邮件主题
            helper.setSubject("域名注册监控：您心仪的域名 " + model.getDomain() + " 现在可以注册辣！");
            //邮件内容
            //获取邮件模板
            String mailHtml = this.getMailHtml(model);
            if (mailHtml == null) {
                log.error("获取到的邮件模板为空");
                throw new RuntimeException("获取到的邮件模板为空");
            }
            helper.setText(mailHtml, true);
            //发送邮件
            mailSender.send(message);
            //记录发送邮件日志
            maillogger.info("Successfully sent an email to " + MAILRECEIVER);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("邮件发送失败", e);
        }
    }

    //邮件模板静态化
    public String getMailHtml(MailTemplateModel mailTemplateModel) {
        try {
            //创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //设置模板路径
            String classpath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates"));
            //设置字符集
            configuration.setDefaultEncoding("utf-8");
            //加载模板
            Template template = configuration.getTemplate("mailTemplate.ftl");
            //模板静态化并返回
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, mailTemplateModel);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("模板静态化异常", e);
            return null;
        }
    }
}
