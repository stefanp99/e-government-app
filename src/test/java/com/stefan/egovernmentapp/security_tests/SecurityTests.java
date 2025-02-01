package com.stefan.egovernmentapp.security_tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUnauthorizedAccessToProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/pending-residents-requests"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSQLInjectionOnLogin() throws Exception {
        String maliciousPayload = "{ \"emailAddress\": \"' OR 1=1 --\", \"password\": \"password\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(maliciousPayload))
                .andExpect(status().isNotFound());
    }

    @Test
    void testBruteForceLogin() throws InterruptedException {
        try (ExecutorService executorService = Executors.newFixedThreadPool(1000)) {
            for (int i = 0; i < 1000; i++) {
                executorService.execute(() -> {
                    try {
                        mockMvc.perform(post("/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{ \"emailAddress\": \"user@example.com\", \"password\": \"wrongPassword\" }"))
                                .andExpect(status().isNotFound());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            executorService.shutdown();
            assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));
        }
    }

}
