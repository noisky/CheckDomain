package me.ffis.checkdomain.service.impl;

import me.ffis.checkdomain.model.MailTemplateModel;
import me.ffis.checkdomain.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by fanfan on 2019/12/05.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
class MailServiceImplTest {

    @Autowired
    private MailService mailService;

    @Test
    void getMailHtmlTest() {
        MailTemplateModel model = new MailTemplateModel();
        model.setDomain("ffis.me");
        model.setTime(new Date());
        model.setStatus("Domain name is available 表示域名可以注册");
        String result = mailService.getMailHtml(model);
        System.out.println(result);
    }

    @Test
    void sendMailTest() {
        MailTemplateModel model = new MailTemplateModel();
        model.setDomain("ffis.me");
        model.setTime(new Date());
        model.setStatus("Domain name is available 表示域名可以注册");
        mailService.sendSimpleMail(model);
    }
}
