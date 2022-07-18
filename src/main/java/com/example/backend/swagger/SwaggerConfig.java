package com.example.backend.swagger;

import com.example.backend.msg.MsgEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    private static final String API_NAME = "TodoWith Project API";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "TodoWith 프로젝트 API 명세서";

    @Bean
    public Docket api() {

        RequestParameterBuilder accessTokenBuilder = new RequestParameterBuilder();
        accessTokenBuilder
                .name(MsgEnum.JWT_HEADER_NAME.getMsg())
                .description("Access Token")
                .required(false)
                .in("header")
                .accepts(Collections.singleton(MediaType.APPLICATION_JSON))
                .build();

        List<RequestParameter> header = new ArrayList<>();
        header.add(accessTokenBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalRequestParameters(header)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(apiKey());
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }

    private List<SecurityScheme> apiKey() {
        List<SecurityScheme>  apiKeys = new ArrayList<>();
        ApiKey access = new ApiKey("Access-Token", MsgEnum.JWT_HEADER_NAME.getMsg(), "header");
        ApiKey refresh = new ApiKey("Refresh-Token", MsgEnum.REFRESH_HEADER_NAME.getMsg(), "header");

        apiKeys.add(access);
        apiKeys.add(refresh);

        return apiKeys;
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> securityReferenceList = new ArrayList<>();

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        AuthorizationScope authorizationScope2 = new AuthorizationScope("global", "refreshEverything");
        AuthorizationScope[] authorizationScopes2 = new AuthorizationScope[1];
        authorizationScopes2[0] = authorizationScope2;


        securityReferenceList.add(new SecurityReference("Access-Token", authorizationScopes));
        securityReferenceList.add(new SecurityReference("Refresh-Token", authorizationScopes2));

        return securityReferenceList;
    }
}