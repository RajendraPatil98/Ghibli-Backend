package in.rajendrapatil.ghibliapi.controller;

import in.rajendrapatil.ghibliapi.dto.PromptEnhanceRequest;
import in.rajendrapatil.ghibliapi.dto.PromptEnhanceResponse;
import in.rajendrapatil.ghibliapi.service.PromptEnhancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
 * Controller for AI-based prompt enhancement using Groq API
 * Endpoint: POST /api/v1/enhance-prompt
 * To remove: Delete this entire file and PromptEnhancerService
 * ===== END PROMPT ENHANCEMENT FEATURE =====
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "https://ghibli-frontend-for-stability-ghibli.onrender.com"})
@RequiredArgsConstructor
public class PromptEnhancerController {

    private final PromptEnhancerService promptEnhancerService;

    // ===== PROMPT ENHANCEMENT FEATURE (AI Feature #1) =====
    @PostMapping("/enhance-prompt")
    public ResponseEntity<PromptEnhanceResponse> enhancePrompt(@RequestBody PromptEnhanceRequest request) {
        try {
            if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            PromptEnhanceResponse response = promptEnhancerService.enhancePrompt(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    // ===== END PROMPT ENHANCEMENT FEATURE =====
}
