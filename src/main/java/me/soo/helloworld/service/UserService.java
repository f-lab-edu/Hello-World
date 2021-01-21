package me.soo.helloworld.service;

import me.soo.helloworld.model.User;
import org.springframework.stereotype.Service;

public interface UserService {
    public void insertUser(User user);
    public boolean isUserIdDuplicate(String userId);
}
