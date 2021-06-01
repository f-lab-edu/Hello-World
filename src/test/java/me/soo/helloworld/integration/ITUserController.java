package me.soo.helloworld.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.model.user.LoginRequest;
import me.soo.helloworld.util.constant.SessionKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static me.soo.helloworld.TestUsersFixture.CURRENT_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
// 테스트용 Transaction 적용: 수동으로 DB에 있는 내용 삭제할 필요없이 각각 독립적인 테스트를 만들기 위해 @Transactional 추가함으로써 롤백기능 부여
@Transactional
@ActiveProfiles("test")
class ITUserController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        httpSession = new MockHttpSession();
    }

    // 매번 중복되는 유저 Sign Up 요청 분리
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
    @DisplayName("회원가입에 성공할 경우 Http Status Code 201(Created)를 리턴합니다.")
    public void userSignUpController() throws Exception {
        testUserSignUp();
    }

    @Test
    @DisplayName("이미 등록되어 있는 아이디일 경우 Http Status Code 409(Conflict)를 리턴합니다.")
    public void duplicateIdCheckTestWithDuplicateID() throws Exception {
        testUserSignUp();

        mockMvc.perform(get("/users/id-check")
                .param("userId", CURRENT_USER.getUserId()))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("등록되어 있는 ID가 아닌 경우 Http Status Code 200(Ok)를 리턴합니다.")
    public void duplicateIdCheckTestWithNoDuplicateId() throws Exception {
        mockMvc.perform(get("/users/id-check")
                .param("userId", CURRENT_USER.getUserId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DB에 등록된 정보와 일치하는 정보를 입력하면 로그인에 성공하고 Http Status Code 200(Ok)를 리턴합니다.")
    public void userLoginSuccess() throws Exception {
        testUserSignUp();

        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                CURRENT_USER.getPassword()
        );

        String loginContent = objectMapper.writeValueAsString(loginRequest);

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
        testUserSignUp();

        httpSession.setAttribute("userId", CURRENT_USER.getUserId());

        LoginRequest loginRequest = new LoginRequest(
                CURRENT_USER.getUserId(),
                CURRENT_USER.getPassword()
        );

        String loginContent = objectMapper.writeValueAsString(loginRequest);

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
        testUserSignUp();

        LoginRequest loginRequest = new LoginRequest(
                "WrongID!@34",
                "WrongPW!@34"
        );

        String loginContent = objectMapper.writeValueAsString(loginRequest);

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
}