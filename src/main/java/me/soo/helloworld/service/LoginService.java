package me.soo.helloworld.service;

import me.soo.helloworld.model.user.LoginRequest;

public interface LoginService {

    public void login(LoginRequest loginRequest);

    public void logout();

    String getCurrentUserId();
}
