package me.soo.helloworld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .gender("Male")
                .birthday(Date.valueOf("1993-09-25"))
                .originCountry("South Korea")
                .livingCountry("United Kingdom")
                .livingTown("Newcastle Upon Tyne")
                .aboutMe("Hello, I'd love to make great friends here")
                .build();

        httpSession = new MockHttpSession();
    }

    @Test
    @DisplayName("회원가입에 성공할 경우 Http Status Code 201(Created)를 리턴합니다.")
    public void userSignUpController() throws Exception {
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이미 등록되어 있는 아이디일 경우 Http Status Code 409(Conflict)를 리턴합니다.")
    public void duplicateIdCheckTestWithDuplicateID() throws Exception {
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

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
        UserLoginInfo testUserLogin = new UserLoginInfo(testUser.getUserId(), testUser.getPassword());

        doReturn(true).when(userRepository).isRegisteredUser(testUserLogin);

        String loginContent = objectMapper.writeValueAsString(testUserLogin);

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
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpSession.setAttribute("userId", testUser.getUserId());

        UserLoginInfo testUserLogin = new UserLoginInfo(testUser.getUserId(), testUser.getPassword());
        String loginContent = objectMapper.writeValueAsString(testUserLogin);

        mockMvc.perform(post("/users/login")
                .content(loginContent)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("등록되지 않은 사용자의 경우 로그인에 실패하며 Http Status Code 401(Unauthorized)를 리턴합니다.")
    public void userLoginFailNoSuchUser() throws Exception {
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        UserLoginInfo testUserLogin = new UserLoginInfo("Hello", "NiceToMeetYou");
        String loginContent = objectMapper.writeValueAsString(testUserLogin);

        mockMvc.perform(post("/users/login")
                .content(loginContent)
                .session(httpSession)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그아웃이 완료되면 Http Status Code 204(No Content)를 리턴합니다.")
    public void userLogoutTest() throws Exception {

        mockMvc.perform(get("/users/logout"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertNull(httpSession.getAttribute("userId"));
    }
}