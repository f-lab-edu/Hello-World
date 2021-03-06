package me.soo.helloworld.model.user;

import lombok.*;
import me.soo.helloworld.model.language.LanguageData;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDataOnProfile {

    private final String userId;

    private final String gender;

    private final int age;

    private final String profileImageName;

    private final String profileImagePath;

    private final String aboutMe;

    private final int originCountryId;

    private final int livingCountryId;

    private final int livingTownId;

    private List<LanguageData> languages;
}
