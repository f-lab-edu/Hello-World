package me.soo.helloworld.util.validator

import me.soo.helloworld.exception.InvalidRequestException

class Validator {

    companion object {
        @JvmStatic
        fun validateTargetNotSelf(userId: String, targetId: String) =
            if (userId == targetId) throw InvalidRequestException("자기 자신에 대해서는 처리할 수 없는 요청입니다.") else Unit

        @JvmStatic
        fun validateTargetExistence(isExist: Boolean) =
            if (!isExist) throw InvalidRequestException("존재하지 않는 사용자에 대해서 처리할 수 없는 요청입니다.") else Unit

        fun validateTargetBlocked(isBlocked: Boolean) =
            if (isBlocked) throw InvalidRequestException("차단되어 있는 사용자에게 친구 요청을 보낼 수 없습니다.") else Unit
    }
}
