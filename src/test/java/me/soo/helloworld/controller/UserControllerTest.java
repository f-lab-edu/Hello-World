package me.soo.helloworld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.soo.helloworld.mapper.UserMapper;
import me.soo.helloworld.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    User testUser;

    @Autowired
    UserMapper userMapper;

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
        String content = objectMapper.writeValueAsString(testUser);
        mockMvc.perform(get("/users/idcheck")
                .param("userId", "gomsu1045"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}