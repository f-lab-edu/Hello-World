package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.soo.helloworld.model.file.FileData;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdate {

    private final String userId;

    private final String gender;

    private final String livingCountry;

    private final String livingTown;

    private final String aboutMe;

    private final String profileImageName;

    private final String profileImagePath;

    public static UserUpdate buildUpdatedUser(String userId,
                                              UserUpdateRequest updateRequest,
                                              FileData profileImageFileInfo) {
        return UserUpdate.builder()
                .userId(userId)
                .gender(updateRequest.getGender())
                .livingCountry(updateRequest.getLivingCountry())
                .livingTown(updateRequest.getLivingTown())
                .aboutMe(updateRequest.getAboutMe())
                .profileImageName(profileImageFileInfo.getFileName())
                .profileImagePath(profileImageFileInfo.getFileDir())
                .build();
    }

}
