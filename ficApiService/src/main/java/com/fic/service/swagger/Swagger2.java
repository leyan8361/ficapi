package com.fic.service.swagger;

import com.fic.service.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *   @Author Xie
 *   @Date 2018/11/21
 *   @Discription:
**/
@Configuration
@EnableSwagger2
//@Profile("dev")
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenParam = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenParam.name(Constants.TOKEN_KEY).description("Token Value")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("").build();
        pars.add(tokenParam.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Api v1 Group")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fic.service.controller.api.v1"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Fic Service Swagger Api v1.0")
                .version("1.0")
                .description("客户端 API 描述")
                .build();
    }

    @Bean
    public Docket createRestApiV2() {
        ParameterBuilder tokenParam = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenParam.name(Constants.TOKEN_KEY).description("Token Value")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("").build();
        pars.add(tokenParam.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Api v2 Group")
                .apiInfo(apiV2Info())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fic.service.controller.api.v2"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiV2Info() {
        return new ApiInfoBuilder()
                .title("Fic Service Swagger Api v2.0")
                .version("2.0")
                .description("客户端 API 描述")
                .build();
    }

    @Bean
    public Docket createBackEndApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("OM Group")
                .apiInfo(backEndInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fic.service.controller"))
                .paths(PathSelectors.regex(".*/backend/.*"))
                .build();
    }

    private ApiInfo backEndInfo() {
        return new ApiInfoBuilder()
                .title("Fic Back End Swagger Api v1.0")
                .version("1.0")
                .description("后台 Api 描述")
                .build();
    }
    
}
