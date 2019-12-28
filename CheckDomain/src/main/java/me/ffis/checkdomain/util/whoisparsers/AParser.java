package me.ffis.checkdomain.util.whoisparsers;

import me.ffis.checkdomain.model.WhoisModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanfan on 2019/12/28.
 */
public abstract class AParser implements Serializable {
    public abstract WhoisModel parseWhois(String whoisResponse);

    protected String getMatchField(Pattern pattern, String source) {
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    protected List<String> getMarchs(Pattern pattern, String source, String delimiter) {
        //获取marcher对象
        Matcher matcher = pattern.matcher(source);
        //创建list数组
        List<String> list = new ArrayList<>();
        //判断是否有匹配
        while (matcher.find()) {
            //获取匹配到的数据
            String group = matcher.group();
            //获取数据中的":"后的数据
            String fieldValue = getFieldValue(group, delimiter);
            //存入list集合
            list.add(fieldValue);
        }
        //将结果返回
        return list;
    }

    protected String getFieldValue(String source, String delimiter) {
        if (source == null || source.isEmpty()) {
            return "";
        }
        return source.split(delimiter, 2)[1].trim();
    }
}
