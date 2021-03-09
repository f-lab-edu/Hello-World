package me.soo.helloworld.service;

import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.exception.DuplicateFriendRequestException;
import me.soo.helloworld.exception.InvalidFriendRequestException;
import me.soo.helloworld.mapper.FriendMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static me.soo.helloworld.enumeration.FriendStatus.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    private final String userId = "Soo1045";

    private final String targetId = "ImNotSoo1045";

    @InjectMocks
    FriendService friendService;

    @Mock
    FriendMapper friendMapper;

    @Mock
    UserService userService;

    /*
        친구 요청을 보낼 때 발생할 수 있는 예외들

        - 요청을 보낼 사용자가 존재하지 않는 경우
        - 친구 요청을 자기 자신에게 보내는 경우
        - 자신이 차단한 사용자에 대해 친구 요청을 보내는 경우
        - 이미 친구 요청을 보낸 유저에게 다시 친구 요청을 보내는 경우
        - 다른 사용자가 보낸 친구 요청을 수락하기 전에 다시 그 사용자에게 친구 요청을 보내는 경우
        - 이미 친구인 유저에게 요청을 다시 보내는 경우
     */
    @Test
    @DisplayName("자기 자신에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToMyselfFail() {
        assertThrows(InvalidFriendRequestException.class, () -> {
           friendService.sendFriendRequest(userId, userId);
        });

        verify(friendMapper, never()).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToNonExistentUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(false);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, never()).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("차단한 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToBlockedUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FriendStatus.BLOCKED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("이미 친구 추가 요청을 보낸 사용자에게 다시 친구 추가 요청을 보낼 경우 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestWithDuplicateFriendRequestFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FriendStatus.REQUESTED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("한 사용자로부터 받은 친구 추가 요청을 수락하지 않은 상태에서 해당 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToWhomAlreadySentMeRequestFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FriendStatus.RECEIVED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("이미 친구 추가 되어 있는 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToAlreadyFriendUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FriendStatus.FRIENDED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("다른 사용자에게 중복된 친구 요청을 보내거나 잘못된 사용자에게 친구요청을 보내는 경우가 아니면 친구 요청을 보내는데 성공합니다.")
    public void sendFriendRequestToValidUserSuccess() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FriendStatus.NOT_YET);

        friendService.sendFriendRequest(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 REQUESTED 인 경우 보낸 친구요청을 철회하는데 성공합니다.")
    public void cancelFriendRequestSuccessWithFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(REQUESTED);

        friendService.cancelFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 BLOCKED 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusBlocked() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(BLOCKED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 NOT_YET 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NOT_YET);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 RECEIVED 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(RECEIVED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 FRIENDED 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIENDED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 RECEIVED 인 경우에 받은 친구요청을 수락하는데 성공합니다.")
    public void acceptFriendRequestSuccessWhenFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(RECEIVED);

        friendService.acceptFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).updateFriendRequest(userId, targetId, FRIENDED);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 BLOCKED 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusBlocked() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(BLOCKED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIENDED);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 NOT_YET 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NOT_YET);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIENDED);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 REQUESTED 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(REQUESTED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIENDED);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 이미 FRIENDED 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusAlreadyFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIENDED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIENDED);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 RECEIVED 인 경우에 받은 친구요청을 거절하는데 성공합니다.")
    public void rejectFriendRequestSuccessWhenFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(RECEIVED);

        friendService.rejectFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 BLOCKED 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 거절하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusBlocked() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(BLOCKED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 NOT_YET 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 거절하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NOT_YET);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 REQUESTED 인 경우 InvalidFriendRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(REQUESTED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 이미 FRIENDED 인 경우 보낸 InvalidFriendRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusAlreadyFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIENDED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("해당 사용자와의 친구 상태가 FRIENDED 인 경우에는 친구 삭제가 성공합니다.")
    public void unfriendFriendSuccessWithFriendedStatusWithTheUser() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIENDED);

        friendService.unfriendFriend(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("해당 사용자와의 친구 상태가 FRIENDED 인 아닌 경우에는 친구 삭제가 실패하며 InvalidFriendRequestException 가 발생합니다.")
    public void unfriendFriendFailWithNotFriendedStatusWithTheUser() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NOT_YET);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.unfriendFriend(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }
}
