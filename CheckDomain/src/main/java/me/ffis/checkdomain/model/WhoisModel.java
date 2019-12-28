package me.ffis.checkdomain.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * WhoisModel 域名信息模型
 * Created by fanfan on 2019/12/28.
 */

@Data
@ToString
public class WhoisModel implements Serializable {
    private String domain;
    private String registrar;
    private String email;
    private String phone;
    private long creatDate;
    private long expireDate;
    private long updateDate;
    private String whoisServer;
    private List<String> NameServer;
    private List<String> domainStatus;
    private Object whiosReponse;
}
