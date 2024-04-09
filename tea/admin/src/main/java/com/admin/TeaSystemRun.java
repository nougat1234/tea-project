package com.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@SpringBootApplication(scanBasePackages = {"com.admin", "com.common"})
@MapperScan("com.admin.**.mapper")
@RestController
public class TeaSystemRun {
    public static void main(String[] args) {
        SpringApplication.run(TeaSystemRun.class, args);
    }

    @GetMapping({"/ping", "/"})
    public String run(HttpServletRequest request) {
        return "后台服务器正常运行 [system]";
    }


    @GetMapping({"/404"})
    public String _404() {
        return "不存在对应资源 [system]";
    }

}
