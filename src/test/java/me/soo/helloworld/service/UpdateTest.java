package me.soo.helloworld.service;


import me.soo.helloworld.model.user.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

/**
 * a temporary test class for 프로파일 이미지 업로드
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UpdateTest {

    @Autowired
    UserService userService;

    @Test
    public void update() throws Exception {
        String userId = "gomsu1045";
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .gender("M")
                .livingCountry("South Korea")
                .livingTown("Gwangju")
                .aboutMe("It's Levio~~~~sa Not Leviosa~~~~")
                .build();

        String fileName = "Honestly.txt";

        MultipartFile multipartFile = new MockMultipartFile("profile", fileName, "text/plain", "Hello There".getBytes());

        userService.userUpdate(userId, multipartFile, updateRequest);

    }

}
