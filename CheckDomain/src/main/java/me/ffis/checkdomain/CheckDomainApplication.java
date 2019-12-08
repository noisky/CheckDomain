package me.ffis.checkdomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //开启异步任务处理
@SpringBootApplication
public class CheckDomainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheckDomainApplication.class, args);
    }
}
