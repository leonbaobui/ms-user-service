package com.twitter.ms.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Parameter authUserId = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name("x-auth-user-id")
                    .description("Authenticated userId")
                    .required(true);
            operation.addParametersItem(authUserId);
            return operation;
        };
    }

    @Bean
    public OpenApiCustomizer openAPIFilter() {
        return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> {
                            if (operation.getOperationId().contains("/ui/v1/auth")) {
                                operation.getParameters().removeIf(parameter -> "x-auth-user-id".equals(parameter.getName()));
                            }
                        }
                );
    }
}