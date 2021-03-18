package me.soo.helloworld.util;

import me.soo.helloworld.exception.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

public class TargetValidator {

    public static void targetNotSelf(String userId, String targetId) {
        if (StringUtils.equals(userId, targetId)) {
            throw new InvalidRequestException("자기 자신에 대해 해당 요청을 처리할 수 없습니다.");
        }
    }

    public static void targetExistence(boolean isTargetUserExist) {
        if (!isTargetUserExist) {
            throw new InvalidRequestException("존재하지 않는 사용자에 대해 해당 요청을 처리할 수 없습니다.");
        }
    }
}
