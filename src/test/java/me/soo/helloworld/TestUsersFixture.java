package me.soo.helloworld;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.soo.helloworld.model.user.User;

import java.sql.Date;

import static me.soo.helloworld.TestCountries.*;
import static me.soo.helloworld.TestTowns.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUsersFixture {

    public static final User CURRENT_USER = new User(
            "soo1045",
            "!@#$!@#$Gomsu1045",
            "soo1045@gmail.com",
            "M",
            Date.valueOf("2009-01-01"),
            SOUTH_KOREA,
            UNITED_KINGDOM,
            NEWCASTLE,
            "Hello, I'd love to make great friends here",
            "328fd95f-e25d-46f3-ab1d-cf0fefbde7ab.jpg",
            "D:\\Project\\Hello-World\\gomsu1045"
    );

    public static final User TENS_MALE_FROM_UK_LIVING_LONDON = new User(
            "tensMale",
            "tensMale",
            "tensMale@gmail.com",
            "M",
            Date.valueOf("2009-01-01"),
            UNITED_KINGDOM,
            UNITED_KINGDOM,
            LONDON
    );

    public static final User TENS_FEMALE_FROM_KOREA = new User(
            "tensFemale",
            "tensFemale",
            "tensFemale@gmail.com",
            "F",
            Date.valueOf("2009-01-01"),
            SOUTH_KOREA,
            SOUTH_KOREA
    );

    public static final User FORTIES_MALE_FROM_UK_LIVING_KOREA_SEOUL = new User(
            "fortiesMale",
            "fortiesMale",
            "fortiesMale@gmail.com",
            "M",
            Date.valueOf("1977-01-01"),
            UNITED_KINGDOM,
            SOUTH_KOREA,
            SEOUL
    );

    public static final User SEVENTIES_FEMALE_FROM_UK_LIVING_LIVERPOOL = new User(
            "seventiesFemale",
            "seventiesFemale",
            "seventiesFemale@gmail.com",
            "F",
            Date.valueOf("1947-01-01"),
            UNITED_KINGDOM,
            UNITED_KINGDOM,
            LIVERPOOL
    );
}
