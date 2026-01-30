package in.rajendrapatil.ghibliapi.controller;

import in.rajendrapatil.ghibliapi.dto.EmotionalAnalysisResponse;
import in.rajendrapatil.ghibliapi.service.EmotionalAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) - TO REMOVE: Delete this entire file =====
 * REST Controller for emotional image analysis
 * 
 * Endpoint: POST /api/v1/analyze-emotion
 * Content-Type: multipart/form-data
 * 
 * Request: Multipart image file under key "image"
 * Response: EmotionalAnalysisResponse JSON with:
 *   - emotions: List of 6 emotion scores (0-10 each)
 *   - dominant_emotion: Primary emotion detected
 *   - key_elements: Color psychology insights
 *   - suggestions: Prompt modification recommendations
 * 
 * Used by: Frontend TextToImageSection & PhotoToImageSection after image generation
 * Dependencies: EmotionalAnalysisService (business logic), EmotionalAnalyzerUtil (image analysis)
 * ===== END EMOTIONAL ANALYZER FEATURE =====
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "https://ghibli-frontend-for-stability-ai-ghibli.onrender.com"})
@RequiredArgsConstructor
public class EmotionalAnalysisController {

    private final EmotionalAnalysisService emotionalAnalysisService;

    // ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) =====
    @PostMapping(value = "/analyze-emotion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmotionalAnalysisResponse> analyzeEmotion(
            @RequestPart("image") MultipartFile imageFile) {
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            byte[] imageBytes = imageFile.getBytes();
            EmotionalAnalysisResponse response = emotionalAnalysisService.analyzeImageEmotion(imageBytes);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    // ===== END EMOTIONAL ANALYZER FEATURE =====
}
