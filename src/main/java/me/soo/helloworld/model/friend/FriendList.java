package me.soo.helloworld.model.friend;

import lombok.Value;
import me.soo.helloworld.enumeration.FriendStatus;

@Value
public class FriendList {

    String friendId;

    FriendStatus status;
}
