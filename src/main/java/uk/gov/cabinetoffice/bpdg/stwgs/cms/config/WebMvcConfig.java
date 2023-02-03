package uk.gov.cabinetoffice.bpdg.stwgs.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.cabinetoffice.bpdg.stwgs.cms.interceptor.RequestInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry
        .addViewController("/webjars/swagger-ui/**")
        .setStatusCode(HttpStatus.NOT_FOUND)
        .setViewName("forward:/resources/error/404.html");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }
}
