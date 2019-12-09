package me.ffis.checkdomain.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Created by fanfan on 2019/12/06.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CheckServiceImplTest {

    @Autowired
    private CheckServiceImpl checkService;

    /*@Test
    void queryFromAliyunTest() {
        String domain = "ffis.me";
        AliYunResponse response = checkService.queryFromAliyunTest(domain);
        System.out.println(response);
    }*/

    @Test
    public void queryFromAliyun() {
        String domain = "ffis.me";
        Map map = checkService.queryFromAliyun(domain);
        System.out.println(map);
    }
}