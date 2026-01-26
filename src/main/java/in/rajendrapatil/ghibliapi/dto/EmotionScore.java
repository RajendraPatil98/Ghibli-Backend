package in.rajendrapatil.ghibliapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) - TO REMOVE: Delete this entire file =====
 * DTO for individual emotion score
 * 
 * Fields:
 * - emotion: Name of emotion (Nostalgia, Serenity, Mystery, Joy, Melancholy, Hope)
 * - score: Numeric score 0-10 representing emotion intensity
 * - description: Brief text explaining why this emotion was detected
 * 
 * Used in: EmotionalAnalysisResponse.emotions list
 * API: POST /api/v1/analyze-emotion
 * ===== END EMOTIONAL ANALYZER FEATURE =====
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionScore {
    private String emotion; // Emotion name (e.g., "Joy", "Nostalgia")
    private int score; // Score 0-10 scale
    @JsonProperty("description")
    private String description; // Why this emotion detected
}
