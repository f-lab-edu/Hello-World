package me.soo.helloworld.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateRequest {

    @Nullable
    private final String gender;

    @Nullable
    private final String livingCountry;

    @Nullable
    private final String livingTown;

    @Nullable
    private final String aboutMe;

}
