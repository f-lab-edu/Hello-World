package me.soo.helloworld;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.soo.helloworld.model.user.User;

import java.sql.Date;

import static me.soo.helloworld.TestCountries.*;
import static me.soo.helloworld.TestTowns.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUsersFixture {

    public static final User CURRENT_USER = User.builder()
            .userId("soo1045")
            .password("soo1045")
            .email("soo1045@gmail.com")
            .gender("M")
            .birthday(Date.valueOf("2009-01-01"))
            .originCountry(SOUTH_KOREA)
            .livingCountry(UNITED_KINGDOM)
            .livingTown(NEWCASTLE)
            .build();

    public static final User TENS_MALE_FROM_UK_LIVING_LONDON = User.builder()
            .userId("tensMale")
            .password("tensMale")
            .email("tensMale@gmail.com")
            .gender("M")
            .birthday(Date.valueOf("2009-01-01"))
            .originCountry(UNITED_KINGDOM)
            .livingCountry(UNITED_KINGDOM)
            .livingTown(LONDON)
            .build();

    public static final User TENS_FEMALE_FROM_KOREA = User.builder()
            .userId("tensFemale")
            .password("tensFemale")
            .email("tensFemale@gmail.com")
            .gender("F")
            .birthday(Date.valueOf("2009-01-01"))
            .originCountry(SOUTH_KOREA)
            .livingCountry(SOUTH_KOREA)
            .build();

    public static final User FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL = User.builder()
            .userId("fortiesMale")
            .password("fortiesMale")
            .email("fortiesMale@gmail.com")
            .gender("M")
            .birthday(Date.valueOf("1977-01-01"))
            .originCountry(UNITED_KINGDOM)
            .livingCountry(SOUTH_KOREA)
            .livingTown(SEOUL)
            .build();

    public static final User SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL = User.builder()
            .userId("seventiesFemale")
            .password("seventiesFemale")
            .email("seventiesFemale@gmail.com")
            .gender("F")
            .birthday(Date.valueOf("1947-01-01"))
            .originCountry(UNITED_KINGDOM)
            .livingCountry(UNITED_KINGDOM)
            .livingTown(LIVERPOOL)
            .build();
}
