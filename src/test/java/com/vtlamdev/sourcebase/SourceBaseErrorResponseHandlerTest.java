package com.vtlamdev.sourcebase;

import com.vtlamdev.sourcebase.exception.ResourceNotFoundException;
import com.vtlamdev.sourcebase.exception.SourceBaseErrorResponseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SourceBaseErrorResponseHandlerTest {

    @Test
    void mapsNotFoundExceptionToStandardErrorPayload() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
                .setControllerAdvice(new SourceBaseErrorResponseHandler())
                .build();

        mockMvc.perform(get("/test/not-found").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/test/not-found"));
    }

    @RestController
    static class ThrowingController {

        @GetMapping("/test/not-found")
        String notFound() {
            throw new ResourceNotFoundException("missing");
        }

    }

}
