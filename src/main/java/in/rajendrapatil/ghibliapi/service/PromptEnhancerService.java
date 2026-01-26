package in.rajendrapatil.ghibliapi.service;

import in.rajendrapatil.ghibliapi.client.GroqClient;
import in.rajendrapatil.ghibliapi.dto.PromptEnhanceRequest;
import in.rajendrapatil.ghibliapi.dto.PromptEnhanceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
 * Service for enhancing user prompts using Groq API (LLM)
 * Converts simple prompts into detailed, art-focused descriptions
 * Graceful fallback: Returns original prompt if API fails or key not set
 * To remove: Delete this file and GroqClient
 * ===== END PROMPT ENHANCEMENT FEATURE =====
 */
@Service
@Slf4j
public class PromptEnhancerService {

    private final GroqClient groqClient;
    private final String groqApiKey;
    private final String groqEnabled;

    public PromptEnhancerService(GroqClient groqClient,
                                @Value("${groq.api.key}") String groqApiKey,
                                @Value("${groq.api.key:optional}") String groqEnabledCheck) {
        this.groqClient = groqClient;
        this.groqApiKey = groqApiKey;
        this.groqEnabled = groqEnabledCheck;
    }

    // ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
    public PromptEnhanceResponse enhancePrompt(PromptEnhanceRequest request) {
        // If Groq API key is not configured, return original prompt with a note
        if (groqApiKey == null || groqApiKey.equals("optional")) {
            log.warn("Groq API key not configured. Returning original prompt. Set GROQ_API_KEY environment variable.");
            return new PromptEnhanceResponse(request.getPrompt());
        }

        try {
            // Create the enhancement prompt
            String enhancementPrompt = buildEnhancementPrompt(request.getPrompt(), request.getStyle());

            // Call Groq API
            GroqClient.GroqRequest groqRequest = new GroqClient.GroqRequest();
            groqRequest.setModel("llama-3.3-70b-versatile"); // Current stable model on Groq
            groqRequest.setTemperature(0.7);
            groqRequest.setMax_tokens(150);

            GroqClient.GroqRequest.Message userMessage = new GroqClient.GroqRequest.Message();
            userMessage.setRole("user");
            userMessage.setContent(enhancementPrompt);

            groqRequest.setMessages(Arrays.asList(userMessage));

            GroqClient.GroqResponse response = groqClient.enhancePrompt(
                    "Bearer " + groqApiKey,
                    groqRequest
            );

            // Extract enhanced prompt from response
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String enhancedPrompt = response.getChoices().get(0).getMessage().getContent().trim();
                log.info("Prompt enhancement successful");
                return new PromptEnhanceResponse(enhancedPrompt);
            }

        } catch (Exception e) {
            log.error("Error enhancing prompt with Groq API: {}", e.getMessage());
        }

        // Fallback: return original prompt if enhancement fails
        log.info("Returning original prompt due to enhancement failure");
        return new PromptEnhanceResponse(request.getPrompt());
    }

    private String buildEnhancementPrompt(String userPrompt, String style) {
        return String.format(
                "You are an expert art prompt engineer. Enhance the following prompt for %s style AI image generation. " +
                "Make it more detailed, descriptive, and focused on visual elements. Keep it under 100 words. " +
                "Return ONLY the enhanced prompt, nothing else.\n\nOriginal prompt: %s",
                style != null && !style.equals("general") ? style : "anime",
                userPrompt
        );
    }
    // ===== END PROMPT ENHANCEMENT FEATURE =====
}
