package me.soo.helloworld.service

import me.soo.helloworld.enumeration.AlarmTypes
import me.soo.helloworld.enumeration.FriendStatus
import me.soo.helloworld.enumeration.FriendStatus.FRIEND
import me.soo.helloworld.enumeration.FriendStatus.FRIEND_REQUESTED
import me.soo.helloworld.enumeration.FriendStatus.FRIEND_REQUEST_RECEIVED
import me.soo.helloworld.enumeration.FriendStatus.NONE
import me.soo.helloworld.exception.DuplicateRequestException
import me.soo.helloworld.exception.InvalidRequestException
import me.soo.helloworld.mapper.FriendMapper
import me.soo.helloworld.model.friend.FriendListRequest
import me.soo.helloworld.model.notification.PushNotificationRequest
import me.soo.helloworld.util.Pagination
import me.soo.helloworld.util.validator.Validator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FriendService(

    private val friendMapper: FriendMapper,

    private val userService: UserService,

    private val blockUserService: BlockUserService,

    private val alarmService: AlarmService,

    private val pushNotificationService: PushNotificationService
) {

    @Transactional
    fun sendFriendRequest(userId: String, targetId: String) {
        Validator.validateTargetNotSelf(userId, targetId)
        Validator.validateTargetExistence(userService.isUserActivated(targetId))
        Validator.validateTargetBlocked(blockUserService.isUserBlocked(userId, targetId))
        validateFriendStatusDetail(getFriendStatus(userId, targetId))

        friendMapper.insertFriendRequest(userId, targetId)
        alarmService.dispatchAlarm(targetId, userId, AlarmTypes.FRIEND_REQUEST_RECEIVED)

        val request = PushNotificationRequest.create(
            targetId,
            AlarmTypes.FRIEND_REQUEST_RECEIVED.name,
            AlarmTypes.FRIEND_REQUEST_RECEIVED.message
        )
        pushNotificationService.sendPushNotification(request)
    }

    @Transactional(readOnly = true)
    fun getFriendStatus(userId: String, targetId: String) = friendMapper.getFriendStatus(userId, targetId)

    @Transactional
    fun cancelFriendRequest(userId: String, targetId: String) {
        validateFriendStatus(getFriendStatus(userId, targetId), FRIEND_REQUESTED)
        friendMapper.deleteFriend(userId, targetId)
        alarmService.removeDispatchedAlarm(targetId, userId, AlarmTypes.FRIEND_REQUEST_RECEIVED)
    }

    @Transactional
    fun acceptFriendRequest(userId: String, targetId: String) {
        validateFriendStatus(getFriendStatus(userId, targetId), FRIEND_REQUEST_RECEIVED)
        friendMapper.updateFriendRequest(userId, targetId, FRIEND)
        alarmService.dispatchAlarm(targetId, userId, AlarmTypes.FRIEND_REQUEST_ACCEPTED)

        val request = PushNotificationRequest.create(
            targetId,
            AlarmTypes.FRIEND_REQUEST_ACCEPTED.name,
            AlarmTypes.FRIEND_REQUEST_ACCEPTED.message
        )
        pushNotificationService.sendPushNotification(request)
    }

    fun rejectFriendRequest(userId: String, targetId: String) {
        validateFriendStatus(getFriendStatus(userId, targetId), FRIEND_REQUEST_RECEIVED)
        friendMapper.deleteFriend(userId, targetId)
    }

    fun unfriendFriend(userId: String, targetId: String) {
        validateFriendStatus(getFriendStatus(userId, targetId), FRIEND)
        friendMapper.deleteFriend(userId, targetId)
    }

    fun getFriendList(userId: String, pagination: Pagination) =
        friendMapper.getFriendList(FriendListRequest.create(userId, pagination, FRIEND))

    fun getFriendRequestList(userId: String, pagination: Pagination) =
        friendMapper.getFriendList(FriendListRequest.create(userId, pagination, FRIEND_REQUEST_RECEIVED))

    fun getFriendshipDuration(userId: String, targetId: String) =
        friendMapper.getFriendshipDuration(userId, targetId, FRIEND)
            ?: throw InvalidRequestException("자기 자신이나 친구가 아닌 대상에 대해서는 해당 요청을 처리하는 것이 불가능합니다.")

    private fun validateFriendStatus(curr: FriendStatus, target: FriendStatus) =
        if (curr != target) throw InvalidRequestException("status 가 $target 인 경우에만 해당 요청에 대해서만 처리가 가능합니다. (현재 status: ($curr))")
        else Unit

    private fun validateFriendStatusDetail(status: FriendStatus) {
        when (status) {
            FRIEND_REQUESTED -> throw DuplicateRequestException("친구요청을 보낸 사용자에게 다시 친구 요청을 보낼 수 없습니다.")
            FRIEND_REQUEST_RECEIVED -> throw DuplicateRequestException("해당 사용자로부터 친구추가 요청을 받은 상태입니다. 받은 친구 요청 목록 창을 확인해주세요.")
            FRIEND -> throw DuplicateRequestException("친구로 등록된 사용자에게 다시 친구 요청을 보낼 수 없습니다.")
            NONE -> Unit
        }
    }
}
