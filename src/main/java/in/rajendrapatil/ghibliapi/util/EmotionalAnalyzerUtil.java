package in.rajendrapatil.ghibliapi.util;

import in.rajendrapatil.ghibliapi.dto.EmotionScore;
import in.rajendrapatil.ghibliapi.dto.EmotionalAnalysisResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) - TO REMOVE: Delete this entire file (293 lines) =====
 * Core utility for analyzing emotional impact of images using advanced image processing:
 * 
 * Analysis Techniques:
 * 1. Color Psychology Analysis:
 *    - Warm colors (R > B + 30): Red/Orange - trigger nostalgia, joy, passion
 *    - Cool colors (B > R + 30): Blue/Purple - create calm, serenity, mystery
 *    - Neutral colors: Similar RGB values - balance, stability
 *    - Muted colors (saturation < 30): Pastel, vintage feel - nostalgia
 * 
 * 2. Brightness Analysis (0-100 scale):
 *    - High brightness (>70): Hope, happiness, light
 *    - Low brightness (<30): Mystery, melancholy, drama
 * 
 * 3. Contrast Analysis (edge differences):
 *    - High contrast (>60): Dramatic, intense, mystery
 *    - Low contrast (<30): Calm, soft, serenity
 * 
 * 4. Saturation Analysis (color intensity):
 *    - High saturation (>60): Energetic, vibrant, joyful
 *    - Low saturation (<30): Calm, vintage, nostalgic
 * 
 * 6 Emotion Scores Calculated:
 *   - Nostalgia: warm (40%) + muted (30%) + low contrast (20%) + low saturation (10%)
 *   - Serenity: cool (30%) + low contrast (40%) + brightness (30%)
 *   - Mystery: dark (40%) + high contrast (30%) + desaturation (30%)
 *   - Joy: brightness (30%) + saturation (40%) + warm (30%)
 *   - Melancholy: cool (30%) + dark (40%) + low contrast (30%)
 *   - Hope: brightness (40%) + warm (40%) + medium contrast (20%)
 * 
 * Additional Output:
 * - Key Elements: Emoji + text insights (e.g., "🔥 Warm tones dominate (triggers nostalgia)")
 * - Suggestions: Actionable prompt modifications to amplify detected emotions
 * 
 * Called by: EmotionalAnalysisService via POST /api/v1/analyze-emotion
 * Input: Image bytes from uploaded or generated image
 * Output: EmotionalAnalysisResponse with emotion scores, dominant emotion, insights, suggestions
 * ===== END EMOTIONAL ANALYZER FEATURE =====
 */
public class EmotionalAnalyzerUtil {

    private static final Map<String, int[]> COLOR_EMOTIONS = new HashMap<>();

    static {
        // Initialize color-emotion mappings (RGB ranges)
        COLOR_EMOTIONS.put("warm", new int[]{255, 100, 0});      // Orange/Red
        COLOR_EMOTIONS.put("cool", new int[]{0, 100, 255});       // Blue
        COLOR_EMOTIONS.put("neutral", new int[]{128, 128, 128}); // Gray
        COLOR_EMOTIONS.put("bright", new int[]{255, 255, 255}); // White
        COLOR_EMOTIONS.put("dark", new int[]{0, 0, 0});           // Black
    }

    /**
     * ===== EMOTIONAL ANALYZER FEATURE (AI Feature #2) =====
     * Analyze emotional impact of an image
     */
    public static EmotionalAnalysisResponse analyzeImageEmotion(byte[] imageBytes) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        
        if (image == null) {
            throw new IOException("Unable to read image");
        }

        // Analyze image properties
        double brightness = calculateBrightness(image);
        Map<String, Integer> colorDistribution = analyzeColorDistribution(image);
        double contrast = calculateContrast(image);
        double saturation = calculateSaturation(image);

        // Calculate emotion scores based on image analysis
        List<EmotionScore> emotions = new ArrayList<>();
        
        // Nostalgia: Warm colors + moderate brightness + soft lighting
        int nostalgia = (int) (
            (colorDistribution.getOrDefault("warm", 0) * 0.4) +
            (colorDistribution.getOrDefault("muted", 0) * 0.3) +
            ((100 - contrast) * 0.2) +
            ((100 - saturation) * 0.1)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Nostalgia", Math.min(10, Math.max(0, nostalgia)), 
            "Warm tones and soft lighting evoke nostalgic feelings"));

        // Serenity: Cool colors + low contrast + high brightness
        int serenity = (int) (
            (colorDistribution.getOrDefault("cool", 0) * 0.3) +
            ((100 - contrast) * 0.4) +
            (brightness * 0.3)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Serenity", Math.min(10, Math.max(0, serenity)),
            "Calm colors and smooth composition create peaceful feeling"));

        // Mystery: Dark areas + high contrast + desaturated colors
        int mystery = (int) (
            ((100 - brightness) * 0.4) +
            (contrast * 0.3) +
            ((100 - saturation) * 0.3)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Mystery", Math.min(10, Math.max(0, mystery)),
            "Dark tones and shadows create sense of mystery"));

        // Joy: Bright + saturated + warm colors
        int joy = (int) (
            (brightness * 0.3) +
            (saturation * 0.4) +
            (colorDistribution.getOrDefault("warm", 0) * 0.3)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Joy", Math.min(10, Math.max(0, joy)),
            "Bright colors and vibrant tones evoke happiness"));

        // Melancholy: Cool colors + low brightness + low contrast
        int melancholy = (int) (
            (colorDistribution.getOrDefault("cool", 0) * 0.3) +
            ((100 - brightness) * 0.4) +
            ((100 - contrast) * 0.3)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Melancholy", Math.min(10, Math.max(0, melancholy)),
            "Cool, dim tones create contemplative mood"));

        // Hope: Bright + warm colors + medium contrast
        int hope = (int) (
            (brightness * 0.4) +
            (colorDistribution.getOrDefault("warm", 0) * 0.4) +
            ((Math.abs(contrast - 50)) * 0.2)
        ) * 10 / 100;
        emotions.add(new EmotionScore("Hope", Math.min(10, Math.max(0, hope)),
            "Light and warmth suggest optimism"));

        // Find dominant emotion
        EmotionScore dominantScore = emotions.stream()
            .max(Comparator.comparingInt(EmotionScore::getScore))
            .orElse(emotions.get(0));

        // Generate key elements and suggestions
        List<String> keyElements = generateKeyElements(colorDistribution, brightness, contrast, saturation);
        List<String> suggestions = generateSuggestions(dominantScore, emotions);

        return new EmotionalAnalysisResponse(emotions, dominantScore.getEmotion(), keyElements, suggestions);
    }
    // ===== END EMOTIONAL ANALYZER FEATURE =====

    private static double calculateBrightness(BufferedImage image) {
        long totalBrightness = 0;
        int pixelCount = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                totalBrightness += (r + g + b) / 3;
                pixelCount++;
            }
        }

        return (totalBrightness / (double) pixelCount) / 255.0 * 100;
    }

    private static Map<String, Integer> analyzeColorDistribution(BufferedImage image) {
        Map<String, Integer> distribution = new HashMap<>();
        int warmCount = 0, coolCount = 0, neutralCount = 0, mutedCount = 0;
        int pixelCount = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Warm colors: R > B
                if (r > b + 30) {
                    warmCount++;
                }
                // Cool colors: B > R
                else if (b > r + 30) {
                    coolCount++;
                }
                // Neutral: similar values
                else {
                    neutralCount++;
                }

                // Muted colors: low saturation
                int max = Math.max(r, Math.max(g, b));
                int min = Math.min(r, Math.min(g, b));
                int saturation = max > 0 ? (max - min) * 100 / max : 0;
                if (saturation < 30) {
                    mutedCount++;
                }

                pixelCount++;
            }
        }

        distribution.put("warm", (warmCount * 100) / pixelCount);
        distribution.put("cool", (coolCount * 100) / pixelCount);
        distribution.put("neutral", (neutralCount * 100) / pixelCount);
        distribution.put("muted", (mutedCount * 100) / pixelCount);

        return distribution;
    }

    private static double calculateContrast(BufferedImage image) {
        long totalContrast = 0;
        int sampleCount = 0;

        int step = Math.max(1, image.getWidth() / 10);
        for (int y = step; y < image.getHeight() - step; y += step) {
            for (int x = step; x < image.getWidth() - step; x += step) {
                int current = getPixelBrightness(image.getRGB(x, y));
                int neighbor = getPixelBrightness(image.getRGB(x + step, y + step));
                totalContrast += Math.abs(current - neighbor);
                sampleCount++;
            }
        }

        return Math.min(100, (totalContrast / (double) sampleCount) / 255.0 * 200);
    }

    private static double calculateSaturation(BufferedImage image) {
        long totalSaturation = 0;
        int pixelCount = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int max = Math.max(r, Math.max(g, b));
                int min = Math.min(r, Math.min(g, b));
                int saturation = max > 0 ? (max - min) * 100 / max : 0;
                totalSaturation += saturation;
                pixelCount++;
            }
        }

        return totalSaturation / (double) pixelCount;
    }

    private static int getPixelBrightness(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (r + g + b) / 3;
    }

    private static List<String> generateKeyElements(Map<String, Integer> colors, 
                                                     double brightness, double contrast, double saturation) {
        List<String> elements = new ArrayList<>();

        if (colors.get("warm") > 40) {
            elements.add("🔥 Warm tones dominate (triggers nostalgia)");
        }
        if (colors.get("cool") > 40) {
            elements.add("❄️ Cool tones dominate (creates calm)");
        }
        if (brightness > 70) {
            elements.add("☀️ High brightness (hope, happiness)");
        }
        if (brightness < 30) {
            elements.add("🌙 Dark tones (mystery, melancholy)");
        }
        if (saturation > 60) {
            elements.add("🎨 Vibrant colors (energetic, joyful)");
        }
        if (saturation < 30) {
            elements.add("📸 Muted colors (calm, nostalgic)");
        }
        if (contrast > 60) {
            elements.add("⚡ High contrast (dramatic, intense)");
        }

        return elements.isEmpty() ? Arrays.asList("🎨 Balanced composition") : elements;
    }

    private static List<String> generateSuggestions(EmotionScore dominant, List<EmotionScore> allEmotions) {
        List<String> suggestions = new ArrayList<>();

        switch (dominant.getEmotion()) {
            case "Nostalgia":
                suggestions.add("✨ Add vintage filter or sepia tones to enhance nostalgia");
                suggestions.add("🌅 Golden hour lighting strengthens the nostalgic feeling");
                break;
            case "Serenity":
                suggestions.add("🍃 Add natural elements (water, trees) for more tranquility");
                suggestions.add("➡️ Use horizontal lines and balanced composition");
                break;
            case "Mystery":
                suggestions.add("👥 Add shadowy figures to increase sense of mystery");
                suggestions.add("🌫️ Include fog, mist, or rain for more intrigue");
                break;
            case "Joy":
                suggestions.add("🎉 Add celebratory elements (festivals, celebrations)");
                suggestions.add("👫 Include multiple happy characters together");
                break;
            case "Melancholy":
                suggestions.add("🌧️ Rainy or overcast settings amplify the mood");
                suggestions.add("🍂 Autumn scenes enhance melancholic feelings");
                break;
            case "Hope":
                suggestions.add("🌅 Add sunrise or light breaking through clouds");
                suggestions.add("✈️ Include upward movement or flying elements");
                break;
        }

        return suggestions;
    }
}
