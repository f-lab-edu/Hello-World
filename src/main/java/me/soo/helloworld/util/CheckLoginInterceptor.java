package me.soo.helloworld.util;

import me.soo.helloworld.annotation.LoginRequired;
import me.soo.helloworld.exception.UnauthorizedAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static me.soo.helloworld.util.http.SessionKeys.USER_ID;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String userId = (String) request.getSession().getAttribute(USER_ID);

        if (handlerMethod.hasMethodAnnotation(LoginRequired.class) && (userId == null)) {
            throw new UnauthorizedAccessException("해당 요청을 처리하기 위해서는 로그인이 먼저 필요합니다.");
        }

        return true;
    }
}
