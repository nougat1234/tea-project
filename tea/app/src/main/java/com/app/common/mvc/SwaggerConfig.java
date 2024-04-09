package com.app.common.mvc;

/**
 * @ClassName SwaggerConfig
 **/
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 创建接口api
     * @return
     */
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2) // 指定生成的文档的类型是Swagger2
//                .pathMapping("/swagger") // 通过接口直接访问swagger，不配置的话默认使用/swagger-ui.html访问
                .select()
                // 生成接口api的方式一
                //.apis(RequestHandlerSelectors.basePackage("org.example.ctrl")) // 需要应用的接口所在的包,可以添加多个在不同包下的ctrl
//                .apis(RequestHandlerSelectors.basePackage("org.example.ctrl"))
                // 生成接口api的方式二
               .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) // 扫描所有使用了@Api注解的接口类,用这种方式生成api更灵活
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * 设置摘要信息
     * @return
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行摘要定制
        return new ApiInfoBuilder()
                .title("接口示例API") // 设置标题
                .description("项目demo接口示例API模板") // 描述
                .contact(new Contact("demoAuth","https://blog.csdn.net/MrBInsomnia?spm=1000.2115.3001.5343","121@163.com")) // 设置作者信息、联系方式：Contact(String name, String blogUrl, String email)
                .version("1.0.0") // 版本
                .build();
    }
}