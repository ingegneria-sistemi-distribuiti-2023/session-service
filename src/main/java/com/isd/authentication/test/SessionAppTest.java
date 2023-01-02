package com.isd.authentication.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class) // FIXME: fa runnare un test che ritorna un errore
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc //need this in Spring Boot test
public class SessionAppTest {
    @Autowired
    MockMvc mockMvc;

//    FIXME: return error
//    @Test
//    public void contextLoads() {
//        Assertions.assertThat(mockMvc).isNot(null);
//    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/api/sessions"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/api/sessions/abc123"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
