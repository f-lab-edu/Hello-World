package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.DuplicateLoginRequestException;
import me.soo.helloworld.model.user.TestUserLoginData;
import me.soo.helloworld.model.user.UserLoginData;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.util.constant.SessionKeys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    private final UserService userService;

    private final PushNotificationService pushNotificationService;

    @Override
    public void login(UserLoginRequest loginRequest) {
        validateDuplicateLogin(getCurrentUserId());

        UserLoginData loginData = userService.getUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword());
        httpSession.setAttribute(SessionKeys.USER_ID, loginData.getUserId());

        updateTokenIfRequired(loginRequest.getUserId(), loginRequest.getToken(), loginData.getToken());
    }

    @Override
    public void testLogin(UserLoginRequest loginRequest) {
        validateDuplicateLogin(getCurrentUserId());

        TestUserLoginData testUserLoginData = userService.testGetUserLoginInfo(loginRequest.getUserId(), loginRequest.getPassword());
        httpSession.setAttribute(SessionKeys.USER_ID, testUserLoginData.getUserId());

//        updateTokenIfRequired(loginRequest.getUserId(), loginRequest.getToken(), loginData.getToken());
    }

    @Override
    public void logout() {
        httpSession.invalidate();
    }

    @Override
    public String getCurrentUserId() {
        return (String) httpSession.getAttribute(SessionKeys.USER_ID);
    }

    private void validateDuplicateLogin(String currentUser) {
        if (currentUser != null) {
            throw new DuplicateLoginRequestException("이미 로그인 되어 있는 사용자입니다.");
        }
    }

    private void updateTokenIfRequired(String userId, String newToken, String oldToken) {
        if (!StringUtils.equals(oldToken, newToken)) {
            pushNotificationService.registerToken(userId, newToken);
        }
    }


}
