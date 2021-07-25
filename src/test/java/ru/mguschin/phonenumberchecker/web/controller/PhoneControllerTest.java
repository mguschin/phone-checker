package ru.mguschin.phonenumberchecker.web.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class PhoneControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void checkReturnAccept() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543210&requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("ACCEPT")));
    }

    @Test
    public void checkReturnChallengeSideA() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543211&requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("CHALLENGE")));
    }

    @Test
    public void checkReturnChallengeSideB() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543212&requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("CHALLENGE")));
    }

    @Test
    public void checkReturnDecline() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543213&requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("DECLINE")));
    }

    @Test
    public void checkNoParameters() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkBlankParameters() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=&requestid=").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkNoPhone() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkNoRequestId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543210").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkInvalidPhone() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=7A87654321&requestid=abc123").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }

    public void checkInvalidrequestId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/phone/check?phone=79876543210&requestid=abc123%").accept(MediaType.TEXT_HTML))
                .andExpect(status().isBadRequest());
    }
}



