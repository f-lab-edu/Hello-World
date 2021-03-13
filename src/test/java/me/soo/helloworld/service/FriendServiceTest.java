package me.soo.helloworld.service;

import me.soo.helloworld.enumeration.FriendStatus;
import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
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
    BlockUserService blockUserService;

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
        assertThrows(InvalidRequestException.class, () -> {
           friendService.sendFriendRequest(userId, userId);
        });

        verify(userService, never()).isUserIdExist(targetId);
        verify(blockUserService, never()).isUserBlocked(userId, targetId);
        verify(friendMapper, never()).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToNonExistentUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService, never()).isUserBlocked(userId, targetId);
        verify(friendMapper, never()).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("차단한 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToBlockedUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserService.isUserBlocked(userId, targetId)).thenReturn(true);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService,times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, never()).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("이미 친구 추가 요청을 보낸 사용자에게 다시 친구 추가 요청을 보낼 경우 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestWithDuplicateFriendRequestFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserService.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUESTED);

        assertThrows(DuplicateRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService,times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("한 사용자로부터 받은 친구 추가 요청을 수락하지 않은 상태에서 해당 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToWhomAlreadySentMeRequestFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserService.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUEST_RECEIVED);

        assertThrows(DuplicateRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService,times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("이미 친구 추가 되어 있는 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToAlreadyFriendUserFail() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserService.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        assertThrows(DuplicateRequestException.class, () -> {
            friendService.sendFriendRequest(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService,times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("다른 사용자에게 중복된 친구 요청을 보내거나 잘못된 사용자에게 친구요청을 보내는 경우가 아니면 친구 요청을 보내는데 성공합니다.")
    public void sendFriendRequestToValidUserSuccess() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserService.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        friendService.sendFriendRequest(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserService,times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).sendFriendRequest(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 REQUESTED 인 경우 보낸 친구요청을 철회하는데 성공합니다.")
    public void cancelFriendRequestSuccessWithFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUESTED);

        friendService.cancelFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 더 이상 존재하지 않는 경우 (NONE), InvalidRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 보낸 친구 요청의 상태가 FRIEND_REQUEST_RECEIVED 인 경우 InvalidRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUEST_RECEIVED);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("이미 친구 상태인 사용자를 대상으로 친구 요청 철회 요청이 들어오는 경우 InvalidRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void cancelFriendRequestFailWithFriendStatusFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.cancelFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 FRIEND_REQUEST_RECEIVED 인 경우에 받은 친구요청을 수락하는데 성공합니다.")
    public void acceptFriendRequestSuccessWhenFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUEST_RECEIVED);

        friendService.acceptFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).updateFriendRequest(userId, targetId, FRIEND);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 더 이상 존재하지 않는 경우 InvalidRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIEND);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 REQUESTED 인 경우에는 InvalidRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUESTED);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIEND);
    }

    @Test
    @DisplayName("사용자가 친구 요청을 보낸 대상과 이미 친구관계가 맺어진 경우 InvalidRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void acceptFriendRequestFailIfFriendStatusAlreadyFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.acceptFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).updateFriendRequest(userId, targetId, FRIEND);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 FRIEND_REQUEST_RECEIVED 인 경우에 받은 친구요청을 거절하는데 성공합니다.")
    public void rejectFriendRequestSuccessWhenFriendStatusReceived() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUEST_RECEIVED);

        friendService.rejectFriendRequest(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 존재하지 않는 경우 InvalidRequestException 이 발생하며 친구요청을 거절하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusNotYet() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 받은 친구 요청의 상태가 REQUESTED 인 경우 InvalidRequestException 이 발생하며 친구요청을 수락하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusRequested() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUESTED);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("사용자가 대상과의 친구 관계가 이미 맺어진 경우에 친구 요청 거절 시도를 하면 InvalidRequestException 이 발생하며 친구요청을 철회하는데 실패합니다.")
    public void rejectFriendRequestFailIfFriendStatusAlreadyFriended() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.rejectFriendRequest(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("해당 사용자와의 친구관계인 경우 친구 삭제가 성공합니다.")
    public void unfriendFriendSuccessWithFriendedStatusWithTheUser() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        friendService.unfriendFriend(userId, targetId);

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
    }

    @Test
    @DisplayName("해당 사용자와의 친구 관계가 아닌 경우에 친구 삭제 요청을 보내면 친구 삭제가 실패하며 InvalidRequestException 가 발생합니다.")
    public void unfriendFriendFailWithNotFriendedStatusWithTheUser() {
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        assertThrows(InvalidRequestException.class, () -> {
            friendService.unfriendFriend(userId, targetId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
    }
}
