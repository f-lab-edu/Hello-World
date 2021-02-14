package me.soo.helloworld.util;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.service.LoginService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Argument Resolver
 * - Controller 메소드의 파라미터에 대해 임의의 값을 주입해주는 역할을 한다.
 * - 메소드의 파라미터 타입, annotation 등을 이용하여 argument resolver 에 대한 적용 여부를 결정할 수 있다.
 *
 * 적용 시점
 * - Client Request -> Dispatcher Servlet -> Request Handler 매핑 -> Handler Adapter 매핑
 *   -> Interceptor 처리 -> ** Argument Resolver 처리 ** (이후 Message Converter 처리 -> Controller Method Invoke)
 *
 * - 코드 중복을 없애기 위해
 *   : 세션에서 현재 로그인 중인 사용자의 정보를 받아 사용하는 일은 지금당장은 회원 비밀번호 업데이트 한 곳이지만
 *   차후에 구현할 회원 정보 업데이트나 회원탈퇴에도 쓰일 것으로 예상
 */
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        return loginService.getCurrentUserId();
    }
}