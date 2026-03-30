package com.example.bankcards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OpenApiGeneratorTest extends BaseTester {

    @Test
    void generateOpenApi() throws Exception {
        Files.createDirectories(Paths.get("docs"));

        var result = mockMvc.perform(get("/v3/api-docs.yaml"))
                .andExpect(status().isOk())
                .andReturn();

        byte[] yamlBytes = result.getResponse().getContentAsByteArray();
        Files.write(Paths.get("docs/openapi.yaml"), yamlBytes);
    }
}
