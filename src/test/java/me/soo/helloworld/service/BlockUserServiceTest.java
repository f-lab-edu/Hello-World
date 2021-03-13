package me.soo.helloworld.service;

import me.soo.helloworld.exception.DuplicateRequestException;
import me.soo.helloworld.exception.InvalidRequestException;
import me.soo.helloworld.mapper.BlockUserMapper;
import me.soo.helloworld.mapper.FriendMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static me.soo.helloworld.enumeration.FriendStatus.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlockUserServiceTest {

    private final String userId = "Soo1045";

    private final String targetId = "I'm Not Soo";

    @InjectMocks
    BlockUserService blockUserService;

    @Mock
    UserService userService;

    @Mock
    FriendMapper friendMapper;

    @Mock
    BlockUserMapper blockUserMapper;

    @Test
    @DisplayName("차단을 시도한 사용자가 자기 자신일 경우 사용자 차단에 실패하며 InvalidRequestException 이 발생합니다.")
    public void blockUserFailIfBlockingMyself() {
        assertThrows(InvalidRequestException.class, () -> {
           blockUserService.blockUser(userId, userId);
        });

        verify(userService, never()).isUserIdExist(targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
        verify(blockUserMapper, never()).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("차단을 시도한 사용자가 존재하지 않을 경우 사용자 차단에 실패하며 InvalidRequestException 이 발생합니다.")
    public void blockUserFailIfBlockingSomeoneNotExist() {
        when(userService.isUserIdExist(targetId)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> {
            blockUserService.blockUser(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
        verify(blockUserMapper, never()).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("이미 차단된 사용자에게 다시 한 번 차단요청을 보내는 경우 사용자 차단에 실패하며 DuplicateRequestException 이 발생합니다.")
    public void blockUserFailIfBlockingSomeoneAlreadyBlocked() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserMapper.isUserBlocked(userId, targetId)).thenReturn(true);

        assertThrows(DuplicateRequestException.class, () -> {
            blockUserService.blockUser(userId, targetId);
        });

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserMapper, times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
        verify(blockUserMapper, never()).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("친구로 등록되어 있는 사용자를 차단할 경우 친구 목록에서 삭제된 후 차단에 성공합니다.")
    public void blockUserSuccessWithFriendUser() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserMapper.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND);

        blockUserService.blockUser(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserMapper, times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
        verify(blockUserMapper, times(1)).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("자신에게 친구 요청을 보낸 사용자를 차단할 경우 친구 목록에서 해당 내역을 삭제된 후 차단에 성공합니다.")
    public void blockUserSuccessWithUserWhoSentFriendRequest() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserMapper.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUEST_RECEIVED);

        blockUserService.blockUser(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserMapper, times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
        verify(blockUserMapper, times(1)).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("자신이 친구 요청을 보낸 사용자를 차단할 경우 친구 목록에서 해당 내역을 삭제된 후 차단에 성공합니다.")
    public void blockUserSuccessWithUserWhoReceivedFriendRequest() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserMapper.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(FRIEND_REQUESTED);

        blockUserService.blockUser(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserMapper, times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, times(1)).deleteFriend(userId, targetId);
        verify(blockUserMapper, times(1)).blockUser(userId, targetId);
    }

    @Test
    @DisplayName("친구로 등록되어 있지 않은 사용자를 차단할 경우 친구 삭제 없이 차단에 성공합니다.")
    public void blockUserSuccessWithNonFriendUser() {
        when(userService.isUserIdExist(targetId)).thenReturn(true);
        when(blockUserMapper.isUserBlocked(userId, targetId)).thenReturn(false);
        when(friendMapper.getFriendStatus(userId, targetId)).thenReturn(NONE);

        blockUserService.blockUser(userId, targetId);

        verify(userService, times(1)).isUserIdExist(targetId);
        verify(blockUserMapper, times(1)).isUserBlocked(userId, targetId);
        verify(friendMapper, never()).deleteFriend(userId, targetId);
        verify(blockUserMapper, times(1)).blockUser(userId, targetId);
    }
}
