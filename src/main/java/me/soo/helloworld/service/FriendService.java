package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.FriendMapper;
import me.soo.helloworld.model.friend.FriendList;
import me.soo.helloworld.model.friend.FriendListRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.soo.helloworld.enumeration.FriendStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;

    private final FriendMapper friendMapper;

    public void sendFriendRequest(String userId, String targetId) {
        confirmNotSelf(userId, targetId);
        confirmTargetExistence(targetId);

        FriendStatus friendStatus = getFriendStatus(userId, targetId);
        validateStatusDetail(friendStatus);

        friendMapper.sendFriendRequest(userId, targetId);
    }

    public FriendStatus getFriendStatus(String userId, String targetId) {
        return friendMapper.getFriendStatus(userId, targetId);
    }

    public void cancelFriendRequest(String userId, String targetId) {
        FriendStatus friendStatus = getFriendStatus(userId, targetId);
        validateStatus(friendStatus, REQUESTED);
        friendMapper.deleteFriend(userId, targetId);
    }

    public void acceptFriendRequest(String userId, String targetId) {
        FriendStatus friendStatus = getFriendStatus(userId, targetId);
        validateStatus(friendStatus, RECEIVED);
        friendMapper.updateFriendRequest(userId, targetId, FRIENDED);
    }

    public void rejectFriendRequest(String userId, String targetId) {
        FriendStatus friendStatus = getFriendStatus(userId, targetId);
        validateStatus(friendStatus, RECEIVED);
        friendMapper.deleteFriend(userId, targetId);
    }

    public void unfriendFriend(String userId, String targetId) {
        FriendStatus friendStatus = getFriendStatus(userId, targetId);
        validateStatus(friendStatus, FRIENDED);
        friendMapper.deleteFriend(userId, targetId);
    }

    public List<FriendList> getFriendList(String userId, int pageNumber) {
        FriendListRequest request = FriendListRequest.create(userId, pageNumber, FRIENDED);
        return friendMapper.getFriendList(request);
    }

    public List<FriendList> getFriendRequestList(String userId, int pageNumber) {
        FriendListRequest request = FriendListRequest.create(userId, pageNumber, RECEIVED);
        return friendMapper.getFriendList(request);
    }

    public void blockUser(String userId, String targetId) {
        confirmNotSelf(userId, targetId);
        confirmTargetExistence(targetId);

        switch (getFriendStatus(userId, targetId)) {
            case BLOCKED:
                throw new DuplicateRequestException("이미 차단된 사용자를 다시 차단할 수 없습니다.");
            case FRIENDED:
                friendMapper.deleteFriend(userId, targetId);
                friendMapper.blockUser(userId, targetId, BLOCKED);
                break;
            case NOT_YET:
            default:
                friendMapper.blockUser(userId, targetId, BLOCKED);
                break;
        }
    }

    private void validateStatusDetail(FriendStatus status) {
        switch (status) {
            case NOT_YET:
                break;
            case REQUESTED:
                throw new DuplicateRequestException("이미 친구요청을 보낸 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case RECEIVED:
                throw new DuplicateRequestException("이미 해당 사용자로부터 친구추가 요청을 받은 상태입니다. 받은 친구 요청을 다시 확인해주세요.");
            case FRIENDED:
                throw new DuplicateRequestException("이미 친구로 등록된 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case BLOCKED:
                throw new InvalidRequestException("차단한 사용자에게 친구 요청을 보낼 수 없습니다.");
        }
    }

    private void validateStatus(FriendStatus currentStatus, FriendStatus targetStatus) {
        if (!currentStatus.equals(targetStatus)) {
            throw new InvalidRequestException("잘못된 status 로의 접근입니다. 해당 요청을 처리할 수 없습니다.");
        }
    }

    private void confirmNotSelf(String userId, String targetId) {
        if (StringUtils.equals(userId, targetId)) {
            throw new InvalidRequestException("자기 자신에 대해 해당 요청을 처리할 수 없습니다.");
        }
    }

    private void confirmTargetExistence(String targetId) {
        if (!userService.isUserIdExist(targetId)) {
            throw new InvalidRequestException("존재하지 않는 사용자에 대해 해당 요청을 처리할 수 없습니다.");
        }
    }
}
