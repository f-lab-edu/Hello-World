package me.soo.helloworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.model.user.UpdateInfoRequest;
import me.soo.helloworld.model.user.UpdatePasswordRequest;
import me.soo.helloworld.util.constant.SessionKeys;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static me.soo.helloworld.TestCountries.*;
import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ITMyPageController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        httpSession = new MockHttpSession();
    }

    private void testUserSignUp() throws Exception {
        String content = objectMapper.writeValueAsString(CURRENT_USER);

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
        testUserSignUp();
        String differentPassword = "!Msugo1@";

        UpdatePasswordRequest newPassword = new UpdatePasswordRequest(
                CURRENT_USER.getPassword(),
                differentPassword,
                differentPassword
        );

        String content = objectMapper.writeValueAsString(newPassword);
        httpSession.setAttribute(SessionKeys.USER_ID, CURRENT_USER.getUserId());

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
        testUserSignUp();

        UpdateInfoRequest updatedUser = new UpdateInfoRequest(
                "M",
                UNITED_KINGDOM,
                OTHERS,
                "I have just been accepted to the Hogwart of Witchcraft and Wizardary"
        );

        String content = objectMapper.writeValueAsString(updatedUser);
        httpSession.setAttribute(SessionKeys.USER_ID, CURRENT_USER.getUserId());

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
        testUserSignUp();

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

        httpSession.setAttribute(SessionKeys.USER_ID, CURRENT_USER.getUserId());

        mockMvc.perform(builders.file(testImageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .session(httpSession))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
