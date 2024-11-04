package com.example.itbangmodkradankanbanapi;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // ตั้งค่าเอกสาร API ทั้งหมด
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Karban API")
                        .description("API documentation for my application")
                        .version("v1.0"));
    }



    @Bean
    public GroupedOpenApi karbanV3() {
        return GroupedOpenApi.builder()
                .group("Karban Api Version 3")
                .pathsToMatch("/v3/**","/login","/token","/v2/colors","/clear-cookie","/validate-token","/user-info") // กำหนด path ที่ต้องการให้แสดงใน Swagger
                .build();
    }

    @Bean
    public GroupedOpenApi endpointForSprint5() {
        return GroupedOpenApi.builder()
                .group("Karban Api for Sprint 5")
                .pathsToMatch("/v3/boards/{nanoId}/tasks/**","/v3/boards/**/collabs","/v3/collabs","/v3/boards/**/tasks/**/attachment","/v3/boards/**/tasks/**/attachment/**","/v3/collabs/receive-invite","/v3/boards/**/invite/**") // กำหนด path ที่ต้องการให้แสดงใน Swagger
                .build();
    }




    @Bean
    public GroupedOpenApi karbanV2() {
        return GroupedOpenApi.builder()
                .group("Karban Api Version 2")
                .pathsToMatch("/v2/**")
                .build();
    }
    @Bean
    public GroupedOpenApi karbanV1() {
        return GroupedOpenApi.builder()
                .group("Karban Api Version 1")
                .pathsToMatch("/v1/**")
                .build();
    }
}
