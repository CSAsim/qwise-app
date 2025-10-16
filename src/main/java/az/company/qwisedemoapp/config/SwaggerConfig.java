//package az.company.qwisedemoapp.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("Qwise Demo API").version("v1"))
////                .addServersItem(new Server().url("https://qwise.codepays.dev/api/v1/qwise-app"))
//                .addServersItem(new Server().url("http://localhost:7775/api/v1/qwise-app"))
//                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
//                .components(new io.swagger.v3.oas.models.Components()
//                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
//                                .name("Authorization")
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                        )
//                );
//    }
//}