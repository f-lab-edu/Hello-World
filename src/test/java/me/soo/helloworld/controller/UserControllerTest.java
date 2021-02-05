package me.soo.helloworld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginRequest;
import me.soo.helloworld.model.user.UserPasswordRequest;
import me.soo.helloworld.model.user.UserUpdateRequest;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.encoder.PasswordEncoder;
import me.soo.helloworld.util.http.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
// 테스트용 Transaction 적용: 수동으로 DB에 있는 내용 삭제할 필요없이 각각 독립적인 테스트를 만들기 위해 @Transactional 추가함으로써 롤백기능 부여
@Transactional
class UserControllerTest {

    User testUser;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .userId("gomsu1045")
                .password("Gomsu1045!0$%")
                .email("test@test.com")
                .gender("M")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry("South Korea")
                .livingCountry("United Kingdom")
                .livingTown("Newcastle Upon Tyne")
                .aboutMe("Hello, I'd love to make great friends here")
                .build();

        httpSession = new MockHttpSession();
    }

    // 매번 중복되는 유저 Sign Up 요청 분리
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
    @DisplayName("회원가입에 성공할 경우 Http Status Code 201(Created)를 리턴합니다.")
    public void userSignUpController() throws Exception {
        testUserSignUp(testUser);
    }

    @Test
    @DisplayName("이미 등록되어 있는 아이디일 경우 Http Status Code 409(Conflict)를 리턴합니다.")
    public void duplicateIdCheckTestWithDuplicateID() throws Exception {
        testUserSignUp(testUser);

        mockMvc.perform(get("/users/idcheck")
                .param("userId", "gomsu1045"))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("등록되어 있는 ID가 아닌 경우 Http Status Code 200(Ok)를 리턴합니다.")
    public void duplicateIdCheckTestWithNoDuplicateId() throws Exception {
        mockMvc.perform(get("/users/idcheck")
                .param("userId", "gomsu1045"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공하고 Http Status Code 200(Ok)를 리턴합니다.")
    public void userLoginSuccess() throws Exception {
        testUserSignUp(testUser);

        UserLoginRequest testLoginRequest = new UserLoginRequest(testUser.getUserId(), testUser.getPassword());

        String loginContent = objectMapper.writeValueAsString(testLoginRequest);

        mockMvc.perform(post("/users/login")
                .content(loginContent)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이미 로그인된 회원의 경우 로그인에 실패하며 Http Status Code 401(Unauthorized)를 리턴합니다.")
    public void userLoginFailAlreadyLogin() throws Exception {
        testUserSignUp(testUser);

        httpSession.setAttribute("userId", testUser.getUserId());

        UserLoginRequest testUserLogin = new UserLoginRequest(testUser.getUserId(), testUser.getPassword());
        String loginContent = objectMapper.writeValueAsString(testUserLogin);

        mockMvc.perform(post("/users/login")
                .content(loginContent)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("등록되지 않은 사용자의 경우 로그인에 실패하며 Http Status Code 404(Not Found)를 리턴합니다.")
    public void userLoginFailNoSuchUser() throws Exception {
        testUserSignUp(testUser);

        UserLoginRequest testUserLogin = new UserLoginRequest("WrongID!@34", "WrongPw!@34");
        String loginContent = objectMapper.writeValueAsString(testUserLogin);

        mockMvc.perform(post("/users/login")
                .content(loginContent)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그아웃이 완료되면 Http Status Code 204(No Content)를 리턴합니다.")
    public void userLogoutTest() throws Exception {

        mockMvc.perform(get("/users/logout"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertNull(httpSession.getAttribute(SessionKeys.USER_ID));
    }

    @Test
    @DisplayName("비밀번호 변경에 성공하면 Http Status Code 200(OK)를 리턴합니다.")
    public void userPasswordUpdateTestSuccess() throws Exception {
        testUserSignUp(testUser);

        String differentPassword = "!Msugo1@";

        UserPasswordRequest newPassword = UserPasswordRequest.builder()
                .currentPassword(testUser.getPassword())
                .newPassword(differentPassword)
                .checkNewPassword(differentPassword)
                .build();

        String content = objectMapper.writeValueAsString(newPassword);
        httpSession.setAttribute(SessionKeys.USER_ID, testUser.getUserId());

        mockMvc.perform(put("/users/account/password")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .session(httpSession))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("유저정보 업데이트에 성공하면 Http Status Code 200(OK)를 리턴합니다.")
    public void userUpdateTestSuccess() throws Exception {
        testUserSignUp(testUser);

        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .gender("F")
                .livingCountry("South Korea")
                .livingTown("Gwangju")
                .aboutMe("I've just back to my hometown from abroad.")
                .build();

        MockMultipartFile testMultipartFile = new MockMultipartFile(
                "profileImage",
                "profileImage",
                "image/jpeg",
                "Hello There".getBytes());

        String content = objectMapper.writeValueAsString(updateRequest);
        httpSession.setAttribute(SessionKeys.USER_ID, testUser.getUserId());

        MockMultipartHttpServletRequestBuilder builders = MockMvcRequestBuilders
                .multipart("/users/account");

        builders.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");

                return request;
            }
        });

        mockMvc.perform(builders.file(testMultipartFile)
                .session(httpSession)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk());
    }

}