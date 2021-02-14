package me.soo.helloworld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.util.http.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MyPageControllerTest {

    User currentUser;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        currentUser = User.builder()
                .userId("gomsu1045")
                .password("Gomsu1045!0$%")
                .email("test@test.com")
                .gender("M")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry("South Korea")
                .livingCountry("United Kingdom")
                .livingTown("Newcastle Upon Tyne")
                .aboutMe("Hello, I'd love to make great friends here")
                .profileImageName("328fd95f-e25d-46f3-ab1d-cf0fefbde7ab.jpg")
                .profileImagePath("D:\\Project\\Hello-World\\gomsu1045")
                .build();

        httpSession = new MockHttpSession();
    }

    private void testUserSignUp(User testUser) throws Exception {
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }



    @Test
    @DisplayName("현재 사용자의 비밀번호 변경에 성공하면 Http Status Code 200(OK)를 리턴합니다.")
    public void userPasswordUpdateTestSuccess() throws Exception {
        testUserSignUp(currentUser);

        String differentPassword = "!Msugo1@";

        UserPasswordRequest newPassword = UserPasswordRequest.builder()
                .currentPassword(currentUser.getPassword())
                .newPassword(differentPassword)
                .checkNewPassword(differentPassword)
                .build();

        String content = objectMapper.writeValueAsString(newPassword);
        httpSession.setAttribute(SessionKeys.USER_ID, currentUser.getUserId());

        mockMvc.perform(put("/my-infos/password")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .session(httpSession))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("현재 사용자의 유저정보 업데이트에 성공하면 Http Status Code 200(OK)를 리턴합니다.")
    public void userInfoUpdateTestSuccess() throws Exception {
        testUserSignUp(currentUser);

        UserUpdateRequest updatedUser = UserUpdateRequest.builder()
                .gender("M")
                .livingCountry("Republic Of Ireland")
                .livingTown("Dublin")
                .aboutMe("I've just moved to Dublin today")
                .build();

        String content = objectMapper.writeValueAsString(updatedUser);
        httpSession.setAttribute(SessionKeys.USER_ID, currentUser.getUserId());

        mockMvc.perform(put("/my-infos")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .session(httpSession))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("현재 사용자의 프로파일 사진 업데이트에 성공하면 Http Status Code 200(OK)를 리턴합니다.")
    public void userProfileUpdateTestSuccess() throws Exception {
        testUserSignUp(currentUser);

        MockMultipartFile testImageFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        MockMultipartHttpServletRequestBuilder builders = MockMvcRequestBuilders
                .multipart("/my-infos/profile-image");

        /*
            기존의 multipart 메소드가 POST 메소드로 고정되어 있기 때문에 PUT 으로 바꿔주기 위해 아래의 방법을 사용
         */
        builders.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");

                return request;
            }
        });

        httpSession.setAttribute(SessionKeys.USER_ID, currentUser.getUserId());

        mockMvc.perform(builders.file(testImageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .session(httpSession))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
