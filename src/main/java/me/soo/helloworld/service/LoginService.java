package me.soo.helloworld.service;

import me.soo.helloworld.model.user.UserLoginInfo;

import javax.servlet.http.HttpSession;

public interface LoginService {

    public void login(UserLoginInfo requestedUserLoginInfo, UserLoginInfo storedUserLoginInfo, HttpSession httpSession);

    public void logout(HttpSession httpSession);
}
