package com.example.bankcards;

import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@Import(TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = MyBank.class, webEnvironment = RANDOM_PORT)
public class BaseTester {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected CardRepository cardRepository;

    @MockBean
    protected TransferRepository transferRepository;

    @MockBean
    protected EntityManagerFactory entityManagerFactory;

    @MockBean
    protected EntityManager entityManager;

    @MockBean
    protected AuthenticationManager authenticationManager;

    @MockBean
    protected Query query;

    protected String asJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
