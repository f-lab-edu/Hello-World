package me.soo.helloworld.service;

import me.soo.helloworld.model.user.UserLoginRequest;

public interface LoginService {

    public void login(UserLoginRequest loginRequest);

    public void logout();

    String getCurrentUserId();
}
