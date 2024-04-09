package com.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@SpringBootApplication(scanBasePackages = {"com.app", "com.common" })
@MapperScan("com.app.**.mapper")
@RestController
public class TeaAppRun {
    public static void main(String[] args) {
        SpringApplication.run(TeaAppRun.class, args);
    }


    @GetMapping({"/ping", "/"})
    public String run(HttpServletRequest request) {
        return "服务器正常运行 [app]";
    }


    @GetMapping({"/404"})
    public String _404() {
        return "不存在对应资源 [tea app]";
    }

}
