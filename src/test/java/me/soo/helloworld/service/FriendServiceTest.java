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

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    private final String userId = "Soo1045";

    private final String anotherUserId = "ImNotSoo1045";

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

        verify(friendMapper, never()).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToNonExistentUserFail() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(false);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, anotherUserId);
        });

        verify(userService, times(1)).doesUserIdExist(anotherUserId);
        verify(friendMapper, never()).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("차단한 사용자에게 친구 추가 요청을 보낼 경우 InvalidRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToBlockedUserFail() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, anotherUserId)).thenReturn(FriendStatus.BLOCKED);

        assertThrows(InvalidFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, anotherUserId);
        });

        verify(userService, times(1)).doesUserIdExist(anotherUserId);
        verify(friendMapper, times(1)).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("이미 친구 추가 요청을 보낸 사용자에게 다시 친구 추가 요청을 보낼 경우 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestWithDuplicateFriendRequestFail() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, anotherUserId)).thenReturn(FriendStatus.REQUESTED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, anotherUserId);
        });

        verify(friendMapper, times(1)).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("한 사용자로부터 받은 친구 추가 요청을 수락하지 않은 상태에서 해당 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToWhomAlreadySentMeRequestFail() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, anotherUserId)).thenReturn(FriendStatus.RECEIVED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, anotherUserId);
        });

        verify(userService, times(1)).doesUserIdExist(anotherUserId);
        verify(friendMapper, times(1)).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("이미 친구 추가 되어 있는 사용자에게 다시 친구 추가 요청을 보내면 DuplicateRequestException 이 발생하며 요청에 실패합니다.")
    public void sendFriendRequestToAlreadyFriendUserFail() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, anotherUserId)).thenReturn(FriendStatus.FRIENDED);

        assertThrows(DuplicateFriendRequestException.class, () -> {
            friendService.sendFriendRequest(userId, anotherUserId);
        });

        verify(userService, times(1)).doesUserIdExist(anotherUserId);
        verify(friendMapper, times(1)).getFriendStatus(userId, anotherUserId);
    }

    @Test
    @DisplayName("다른 사용자에게 중복된 친구 요청을 보내거나 잘못된 사용자에게 친구요청을 보내는 경우가 아니면 친구 요청을 보내는데 성공합니다.")
    public void sendFriendRequestToValidUserSuccess() {
        when(userService.doesUserIdExist(anotherUserId)).thenReturn(true);
        when(friendMapper.getFriendStatus(userId, anotherUserId)).thenReturn(FriendStatus.NOT_YET);

        friendService.sendFriendRequest(userId, anotherUserId);

        verify(userService, times(1)).doesUserIdExist(anotherUserId);
        verify(friendMapper, times(1)).getFriendStatus(userId, anotherUserId);
    }
}
