package me.ffis.checkdomain.model;

import lombok.Data;

import java.util.Date;

/**
 * 邮件模板静态化模型数据
 * Created by fanfan on 2019/12/05.
 */
@Data
public class MailTemplateModel {
    private String domain;
    private Date time;
    private String status;
}
