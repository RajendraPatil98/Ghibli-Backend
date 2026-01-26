package in.rajendrapatil.ghibliapi.service;


import in.rajendrapatil.ghibliapi.client.StabiltyAIClient;
import in.rajendrapatil.ghibliapi.dto.TextToImageRequest;
import in.rajendrapatil.ghibliapi.util.ImageResizingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GhibliArtService {

    private final StabiltyAIClient stabiltyAIClient;
    private final String apiKey;

    public GhibliArtService(StabiltyAIClient stabiltyAIClient, @Value("${stability.api.key}") String apiKey) {
        this.stabiltyAIClient = stabiltyAIClient;
        this.apiKey = apiKey;
    }

    public byte[] createGhibliArt(MultipartFile image, String prompt) throws IOException {
        String finalPrompt =prompt+", in the beautiful, detailed anieme style of studio ghibli.";
        String engineId="stable-diffusion-xl-1024-v1-0";
        String stylePreset="anime";

        System.out.println("Original image size: " + (image.getSize() / 1024.0 / 1024.0) + " MB");

        // Resize image if needed to match Stability AI API requirements
        MultipartFile resizedImage = ImageResizingUtil.resizeImageIfNeeded(image);
        
        System.out.println("After resize image size: " + (resizedImage.getSize() / 1024.0 / 1024.0) + " MB");

        return stabiltyAIClient.generateImageFromImage("Bearer "+apiKey,engineId,resizedImage,finalPrompt,stylePreset);
    }

    public byte[] createGhibliArtFromText(String prompt, String style) {
        String finalPrompt =prompt+", in the beautiful, detailed anieme style of studio ghibli.";
        String engineId="stable-diffusion-xl-1024-v1-0";
        String stylePreset=style.equals("general")?"anime":style.replace("_","-");

        TextToImageRequest requestPayload=new TextToImageRequest(finalPrompt,stylePreset);
        return stabiltyAIClient.generateImageFromText("Bearer "+apiKey,engineId,requestPayload);
    }
}
