package me.soo.helloworld.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import me.soo.helloworld.util.http.SessionKeys;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SessionLoginService implements LoginService {

    private final HttpSession httpSession;

    public void login(String userId) {

        if (getCurrentUserId() != null) {
            throw new DuplicateRequestException("이미 로그인 되어 있습니다.");
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
