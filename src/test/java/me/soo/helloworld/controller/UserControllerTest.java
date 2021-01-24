package me.soo.helloworld.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.user.User;
import me.soo.helloworld.model.user.UserLoginInfo;
import me.soo.helloworld.repository.UserRepository;
import me.soo.helloworld.service.UserService;
import me.soo.helloworld.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest @AutoConfigureMockMvc
class UserControllerTest {
    User testUser;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

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
    }

    @Test
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
    public void duplicateIdCheckTest() throws Exception {
        String content = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(post("/users/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/users/idCheck")
                .param("userId", "gomsu1045"))
                .andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    public void userLogin() throws Exception {
        // 1. Login success
        UserLoginInfo testUserLogin = new UserLoginInfo("msugo10451045", "Gomsu1045!0$%");
        String content = objectMapper.writeValueAsString(testUserLogin);

        mockMvc.perform(post("/users/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // 2. Login fail.
        // 2-1) already login.
        // 2-2) wrong id.
    }
}