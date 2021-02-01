package me.soo.helloworld.util;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.annotation.CurrentUser;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.service.LoginService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Argument Resolver: Controller 메소드 안의 파라미터 값으로 임의의 값을 전달하기 위해 사용
 * 현재는 유저 비밀번호 업데이트 하나지만 앞으로 세션에서 현재 유저의 정보를 더 얻어올 일이 있을 것 같아 중복을 피할 방법을 찾다가 발견
 * 구현/테스트 후 Argument Resolver 가 무엇인지 찾아서 업데이트 해놓자.
 */
@Component
@RequiredArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    private final UserMapper userMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String currentUserId = loginService.getCurrentUserId();
        User currentUser = userMapper.getUserById(currentUserId);

        if (currentUser == null) throw new IllegalArgumentException("잘못된 접근입니다.");
        return currentUser;
    }
}