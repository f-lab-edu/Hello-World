package me.soo.helloworld.service;

import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import javax.servlet.http.HttpSession;

public interface UserService {
    public void userSignUp(User user);
    public boolean isUserIdDuplicate(String userId);
    public void loginRequest(UserLoginInfo userLoginInfo, HttpSession httpSession);
    public void logoutRequest(HttpSession httpSession);
}
