package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.model.user.UserIdAndPassword;
import me.soo.helloworld.util.PasswordEncoder;
import me.soo.helloworld.util.SessionKeys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final PasswordEncoder passwordEncoder;

    private final HttpSession httpSession;

    public void login(UserIdAndPassword requestedUserIdAndPassword, UserIdAndPassword storedUserIdAndPassword) {

        if (httpSession.getAttribute(SessionKeys.USER_ID) != null) {
            throw new DuplicateRequestException("이미 로그인 되어 있습니다.");
        }

        boolean isUserPasswordMatch = passwordEncoder.isMatch(requestedUserIdAndPassword.getPassword(), storedUserIdAndPassword.getPassword());

        if (!isUserPasswordMatch)  {
            throw new IllegalArgumentException("비밀번호를 다시 한 번 확인해주세요.");
        }

        httpSession.setAttribute(SessionKeys.USER_ID, requestedUserIdAndPassword.getUserId());
    }

    public void logout() {
        httpSession.invalidate();
    }

}
