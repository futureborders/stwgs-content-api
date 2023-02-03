package uk.gov.cabinetoffice.bpdg.stwgs.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerInterceptor;

public class RequestInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String theMethod = request.getMethod();
    if (!HttpMethod.GET.matches(theMethod)) {
      throw new HttpRequestMethodNotSupportedException(theMethod);
    }
    return true;
  }
}
