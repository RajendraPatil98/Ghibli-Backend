package in.rajendrapatil.ghibliapi.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
 * Feign client for Groq API integration
 * Used for LLM-based prompt enhancement
 * To remove: Delete this file
 * ===== END PROMPT ENHANCEMENT FEATURE =====
 */
@FeignClient(name = "groqClient", url = "https://api.groq.com/openai/v1")
public interface GroqClient {

    // ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
    @PostMapping(value = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE)
    GroqResponse enhancePrompt(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody GroqRequest request
    );
    // ===== END PROMPT ENHANCEMENT FEATURE =====

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GroqRequest {
        private String model;
        private java.util.List<Message> messages;
        private double temperature;
        private int max_tokens;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Message {
            private String role;
            private String content;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GroqResponse {
        private java.util.List<Choice> choices;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Choice {
            private Message message;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Message {
                private String content;
            }
        }
    }
}
