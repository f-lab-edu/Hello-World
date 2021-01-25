package me.soo.helloworld.service;

import me.soo.helloworld.model.User;

public interface UserService {
    public void userSignUp(User user);
    public boolean isUserIdDuplicate(String userId);
}
