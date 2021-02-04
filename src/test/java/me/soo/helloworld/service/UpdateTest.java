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
                .gender("Female")
                .livingCountry("Bellatrix")
                .livingTown("Hermione Granger")
                .aboutMe("Hosanna In the Highest")
                .build();

        String fileName = "I am ground.jpg";

        MultipartFile multipartFile = new MockMultipartFile("profile", fileName, "text/plain", "Hello There".getBytes());

        userService.userInfoUpdate(userId, multipartFile, updateRequest);

    }

}
