package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.DuplicateLoginRequestException;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.util.constant.SessionKeys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    private final UserService userService;

    public void login(UserLoginRequest loginRequest) {

        if (getCurrentUserId() != null) {
            throw new DuplicateLoginRequestException("이미 로그인 되어 있는 사용자입니다.");
        }

        User user = userService.getUser(loginRequest.getUserId(), loginRequest.getPassword());

        httpSession.setAttribute(SessionKeys.USER_ID, user.getUserId());
    }

    public void logout() {
        httpSession.invalidate();
    }

    @Override
    public String getCurrentUserId() {
        return (String) httpSession.getAttribute(SessionKeys.USER_ID);
    }


}
