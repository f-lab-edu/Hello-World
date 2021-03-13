package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.mapper.BlockUserMapper;
import me.soo.helloworld.mapper.FriendMapper;
import me.soo.helloworld.util.TargetValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockUserService {

    private final UserService userService;

    private final FriendMapper friendMapper;

    private final BlockUserMapper blockUserMapper;

    public void blockUser(String userId, String targetId) {
        TargetValidator.targetNotSelf(userId, targetId);
        TargetValidator.targetExistence(userService.isUserIdExist(targetId));

        if (isUserBlocked(userId, targetId)) {
            throw new DuplicateRequestException("이미 차단된 사용자를 다시 차단할 수 없습니다.");
        }

        switch (friendMapper.getFriendStatus(userId, targetId)) {
            case FRIEND:
            case FRIEND_REQUESTED:
            case FRIEND_REQUEST_RECEIVED:
                friendMapper.deleteFriend(userId, targetId);
                blockUserMapper.blockUser(userId, targetId);
                break;
            case NONE:
            default:
                blockUserMapper.blockUser(userId, targetId);
                break;
        }
    }

    public boolean isUserBlocked(String userId, String targetId) {
        return blockUserMapper.isUserBlocked(userId, targetId);
    }
}
