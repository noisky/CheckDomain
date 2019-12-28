package me.ffis.checkdomain.service.impl;

import me.ffis.checkdomain.model.WhoisModel;
import me.ffis.checkdomain.util.WhoisUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanfan on 2019/12/15.
 */
public class WhoisTest {

    @Test
    public void test1() {
        long start = System.currentTimeMillis();
//        WhoisModel result = WhoisUtil.queryWhois("http://www.baidu.com");
//        WhoisModel result = WhoisUtil.queryWhois("123.com");
        WhoisModel result = WhoisUtil.queryWhois("123.net");
//        WhoisModel result = WhoisUtil.queryWhois("123.cn");
//        WhoisModel result = WhoisUtil.queryWhois("123.org");
//        WhoisModel result = WhoisUtil.queryWhois("123.me");
        long end = System.currentTimeMillis();
        System.out.println("查询耗时：" + (end - start) + "ms");
        System.out.println(result);
    }

    private static String teststr = "11 11 11 11 11";

    @Test
    public void testa() {
        Pattern wp = Pattern.compile("11");
        Matcher m = wp.matcher(teststr);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        for (String s : list) {
            System.out.println(s);
        }

    }

}
