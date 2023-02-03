package uk.gov.cabinetoffice.bpdg.stwgs.cms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("STW-GS Content API").license(new License().name("Apache 2.0")))
        .addServersItem(new Server().url("/"));
  }

  @Bean
  @Primary
  public SwaggerUiConfigProperties swaggerUiConfigProperties() {
    SwaggerUiConfigProperties swaggerUiConfigProperties = new SwaggerUiConfigProperties();
    swaggerUiConfigProperties.setDisableSwaggerDefaultUrl(true);
    swaggerUiConfigProperties.setOperationsSorter("alpha");
    swaggerUiConfigProperties.setFilter("true");
    return swaggerUiConfigProperties;
  }
}
