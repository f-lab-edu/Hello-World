package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.FriendMapper;
import me.soo.helloworld.model.friend.FriendList;
import me.soo.helloworld.model.friend.FriendListRequest;
import me.soo.helloworld.util.TargetValidator;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.soo.helloworld.enumeration.FriendStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;

    private final FriendMapper friendMapper;

    private final BlockUserService blockUserService;

    public void sendFriendRequest(String userId, String targetId) {
        TargetValidator.targetNotSelf(userId, targetId);
        TargetValidator.targetExistence(userService.isUserIdExist(targetId));

        if (blockUserService.isUserBlocked(userId, targetId)) {
            throw new InvalidRequestException("차단되어 있는 사용자에게 친구 요청을 보낼 수 없습니다.");
        }

        FriendStatus status = getFriendStatus(userId, targetId);
        validateFriendStatusDetail(status);

        friendMapper.sendFriendRequest(userId, targetId);
    }

    public FriendStatus getFriendStatus(String userId, String targetId) {
        return friendMapper.getFriendStatus(userId, targetId);
    }

    public void cancelFriendRequest(String userId, String targetId) {
        FriendStatus status = getFriendStatus(userId, targetId);
        validateFriendStatus(status, FRIEND_REQUESTED);
        friendMapper.deleteFriend(userId, targetId);
    }

    public void acceptFriendRequest(String userId, String targetId) {
        FriendStatus status = getFriendStatus(userId, targetId);
        validateFriendStatus(status, FRIEND_REQUEST_RECEIVED);
        friendMapper.updateFriendRequest(userId, targetId, FRIEND);
    }

    public void rejectFriendRequest(String userId, String targetId) {
        FriendStatus status = getFriendStatus(userId, targetId);
        validateFriendStatus(status, FRIEND_REQUEST_RECEIVED);
        friendMapper.deleteFriend(userId, targetId);
    }

    public void unfriendFriend(String userId, String targetId) {
        FriendStatus status = getFriendStatus(userId, targetId);
        validateFriendStatus(status, FRIEND);
        friendMapper.deleteFriend(userId, targetId);
    }

    public List<FriendList> getFriendList(String userId, int pageNumber) {
        FriendListRequest request = FriendListRequest.create(userId, pageNumber, FRIEND);
        return friendMapper.getFriendList(request);
    }

    public List<FriendList> getFriendRequestList(String userId, int pageNumber) {
        FriendListRequest request = FriendListRequest.create(userId, pageNumber, FRIEND_REQUEST_RECEIVED);
        return friendMapper.getFriendList(request);
    }

    private void validateFriendStatus(FriendStatus currentStatus, FriendStatus targetStatus) {
        if (!currentStatus.equals(targetStatus)) {
            throw new InvalidRequestException("잘못된 status 로 부터의 접근입니다. 해당 요청을 처리할 수 없습니다.");
        }
    }

    private void validateFriendStatusDetail(FriendStatus status) {
        switch (status) {
            case FRIEND_REQUESTED:
                throw new DuplicateRequestException("이미 친구요청을 보낸 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case FRIEND_REQUEST_RECEIVED:
                throw new DuplicateRequestException("이미 해당 사용자로부터 친구추가 요청을 받은 상태입니다. 받은 친구 요청을 다시 확인해주세요.");
            case FRIEND:
                throw new DuplicateRequestException("이미 친구로 등록된 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case NONE:
            default:
                break;
        }
    }
}
