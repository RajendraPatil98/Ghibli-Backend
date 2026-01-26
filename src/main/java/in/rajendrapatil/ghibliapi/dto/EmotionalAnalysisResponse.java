package in.rajendrapatil.ghibliapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) - TO REMOVE: Delete this entire file =====
 * DTO for emotional analysis response from backend API
 * 
 * Response structure returned by POST /api/v1/analyze-emotion endpoint
 * 
 * Fields:
 * - emotions: List of EmotionScore objects (6 emotions: Nostalgia, Serenity, Mystery, Joy, Melancholy, Hope)
 * - dominantEmotion: The highest-scoring emotion
 * - keyElements: List of insights about image composition (emojis + descriptions)
 * - suggestions: List of prompt modifications to amplify dominant emotion
 * 
 * Backend Logic: EmotionalAnalyzerUtil.analyzeImageEmotion() analyzes:
 *   1. Color distribution (warm/cool/neutral/muted percentages)
 *   2. Brightness (0-100 scale)
 *   3. Contrast (edge differences)
 *   4. Saturation (color intensity)
 * ===== END EMOTIONAL ANALYZER FEATURE =====
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionalAnalysisResponse {
    @JsonProperty("emotions")
    private List<EmotionScore> emotions; // List of 6 emotion scores
    
    @JsonProperty("dominant_emotion")
    private String dominantEmotion; // Highest-scoring emotion name
    
    @JsonProperty("key_elements")
    private List<String> keyElements; // Color psychology insights (e.g., "🔥 Warm tones dominate")
    
    @JsonProperty("suggestions")
    private List<String> suggestions; // How to modify prompt to amplify emotions
}
