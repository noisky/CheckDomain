package me.ffis.checkdomain;

import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by fanfan on 2019/12/04.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Test
    public void test() {
        System.out.println(username + ", " + password);
    }

}
