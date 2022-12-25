package com.server.simple.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.simple.domain.Session;
import com.server.simple.domain.User;
import com.server.simple.repository.SessionRepository;
import com.server.simple.repository.UserRepository;
import com.server.simple.request.Login;
import com.server.simple.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("simple")
                .email("simple@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("simple@gmail.com")
                .password("1234")
                .build();


        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @Transactional
    @DisplayName("로그인 성공후 세션 1개 생성")
    void test2() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("simple")
                .email("simple@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("simple@gmail.com")
                .password("1234")
                .build();


        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

//        User loggedInUser = userRepository.findById(user.getId())
//                        .orElseThrow(RuntimeException::new);

        Assertions.assertEquals(1, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공후 세션 생성")
    void test3() throws Exception {
        //given
        User user = User.builder()
                .name("simple")
                .email("simple@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());


    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /foo")
    void test4() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("simple")
                .email("simple@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("simple@gmail.com")
                .password("1234")
                .build();


        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 요청하면 오류")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .name("simple")
                .email("simple@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken()+"-error")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }
}