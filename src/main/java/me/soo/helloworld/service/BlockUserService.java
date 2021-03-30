package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.BlockUserMapper;
import me.soo.helloworld.mapper.FriendMapper;
import me.soo.helloworld.model.blockuser.BlockUserList;
import me.soo.helloworld.model.blockuser.BlockUserListRequest;
import me.soo.helloworld.util.Pagination;
import me.soo.helloworld.util.TargetValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockUserService {

    private final UserService userService;

    private final FriendMapper friendMapper;

    private final BlockUserMapper blockUserMapper;

    public void blockUser(String userId, String targetId) {
        TargetValidator.targetNotSelf(userId, targetId);
        TargetValidator.targetExistence(userService.isUserActivated(targetId));

        if (isUserBlocked(userId, targetId)) {
            throw new DuplicateRequestException("이미 차단된 사용자를 다시 차단할 수 없습니다.");
        }

        switch (friendMapper.getFriendStatus(userId, targetId)) {
            case FRIEND:
            case FRIEND_REQUESTED:
            case FRIEND_REQUEST_RECEIVED:
                friendMapper.deleteFriend(userId, targetId);
                blockUserMapper.insertBlockUser(userId, targetId);
                break;
            case NONE:
            default:
                blockUserMapper.insertBlockUser(userId, targetId);
                break;
        }
    }

    public boolean isUserBlocked(String userId, String targetId) {
        return blockUserMapper.isUserBlocked(userId, targetId);
    }

    public void unblockUser(String userId, String targetId) {
        if (!isUserBlocked(userId, targetId)) {
            throw new InvalidRequestException("차단되어 있지 않은 사용자를 차단해제 할 수 없습니다. 다시 한 번 확인해주세요.");
        }

        blockUserMapper.deleteBlockUser(userId, targetId);
    }

    @Transactional(readOnly = true)
    public List<BlockUserList> getBlockUserList(String userId, Pagination pagination) {
        BlockUserListRequest request = BlockUserListRequest.create(userId, pagination);
        return blockUserMapper.getBlockUserList(request);
    }
}
