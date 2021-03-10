package me.soo.helloworld.service;

import lombok.RequiredArgsConstructor;
import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.exception.DuplicateFriendRequestException;
import me.soo.helloworld.exception.InvalidFriendRequestException;
import me.soo.helloworld.mapper.FriendMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static me.soo.helloworld.enumeration.FriendStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserService userService;

    private final FriendMapper friendMapper;

    public void sendFriendRequest(String userId, String targetId) {
        if (StringUtils.equals(userId, targetId)) {
            throw new InvalidFriendRequestException("자기 자신에게는 친구추가 요청을 보낼 수 없습니다.");
        }

        if (!userService.isUserIdExist(targetId)) {
            throw new InvalidFriendRequestException("존재하지 않는 사용자에게 친구추가 요청을 보낼 수 없습니다.");
        }

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

    private void validateStatusDetail(FriendStatus status) {
        switch (status) {
            case NOT_YET:
                break;
            case REQUESTED:
                throw new DuplicateFriendRequestException("이미 친구요청을 보낸 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case RECEIVED:
                throw new DuplicateFriendRequestException("이미 해당 사용자로부터 친구추가 요청을 받은 상태입니다. 받은 친구 요청을 다시 확인해주세요.");
            case FRIENDED:
                throw new DuplicateFriendRequestException("이미 친구로 등록된 사용자에게 다시 친구 요청을 보낼 수 없습니다.");
            case BLOCKED:
                throw new InvalidFriendRequestException("차단한 사용자에게 친구 요청을 보낼 수 없습니다.");
        }
    }

    private void validateStatus(FriendStatus currentStatus, FriendStatus targetStatus) {
        if (!currentStatus.equals(targetStatus)) {
            throw new InvalidFriendRequestException("잘못된 status 로의 접근입니다. 해당 요청을 처리할 수 없습니다.");
        }
    }
}
