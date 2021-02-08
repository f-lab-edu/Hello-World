package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enums.LoginExceptionCode;
import me.soo.helloworld.exception.LoginException;
import me.soo.helloworld.util.http.SessionKeys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    public void login(String userId) {

        if (getCurrentUserId() != null) {
            throw new LoginException(LoginExceptionCode.DUPLICATE_LOGIN_REQUEST);
        }

        httpSession.setAttribute(SessionKeys.USER_ID, userId);
    }

    public void logout() {
        httpSession.invalidate();
    }

    @Override
    public String getCurrentUserId() {
        return (String) httpSession.getAttribute(SessionKeys.USER_ID);
    }


}
