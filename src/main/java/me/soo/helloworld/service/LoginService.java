package me.soo.helloworld.service;

import me.soo.helloworld.model.user.UserIdAndPassword;

public interface LoginService {

    public void login(UserIdAndPassword requestedUserLoginInfo, UserIdAndPassword storedUserLoginInfo);

    public void logout();
}
