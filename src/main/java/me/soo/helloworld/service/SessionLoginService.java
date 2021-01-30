package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.util.PasswordEncoder;
import me.soo.helloworld.util.SessionKeys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final PasswordEncoder passwordEncoder;

    public void login(UserLoginInfo requestedUserLoginInfo, UserLoginInfo storedUserLoginInfo, HttpSession httpSession) {

        if (httpSession.getAttribute(SessionKeys.USER_ID) != null) {
            throw new DuplicateRequestException("이미 로그인 되어 있습니다.");
        }

        loginValidationCheck(requestedUserLoginInfo, storedUserLoginInfo);

        httpSession.setAttribute(SessionKeys.USER_ID, requestedUserLoginInfo.getUserId());
    }

    public void logout(HttpSession httpSession) {

        httpSession.invalidate();

    }

    private void loginValidationCheck(UserLoginInfo requestedUserLoginInfo, UserLoginInfo storedUserLoginInfo) {

        if (storedUserLoginInfo == null) {
            throw new IllegalArgumentException("해당 유저는 존재하지 않습니다.");
        }

        boolean isUserPasswordMatch = passwordEncoder.isMatch(requestedUserLoginInfo.getPassword(), storedUserLoginInfo.getPassword());

        if (!isUserPasswordMatch)  {
            throw new IllegalArgumentException("비밀번호를 다시 한 번 확인해주세요.");
        }

    }
}
