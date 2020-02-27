package me.ffis.checkdomain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    private String whoisServer;
    private List<String> NameServer;
    private List<String> domainStatus;
    private Object whiosReponse;
}
