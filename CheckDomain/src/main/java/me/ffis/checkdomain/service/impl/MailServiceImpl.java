package me.ffis.checkdomain.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件发送服务
 * Created by fanfan on 2019/12/05.
 */
@Service
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
                throw new RuntimeException("邮件收信人不能为空");
            }
            //邮件发送人
            helper.setFrom(MAILSENDER);
            //邮件接收人
            helper.setTo(MAILRECEIVER);
            //邮件主题
            helper.setSubject("域名注册监控： 您心仪的域名 " + model.getDomain() + " 现在可以注册辣！");
            //邮件内容
            //获取邮件模板
            String mailHtml = this.getMailHtml(model);
            if (mailHtml == null) {
                throw new RuntimeException("模板静态化异常");
            }
            helper.setText(mailHtml, true);
            //发送邮件
            mailSender.send(message);
            logger.info("邮件已发送");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("邮件发送失败", e);
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
            logger.error("模板静态化异常", e);
            return null;
        }
    }
}
