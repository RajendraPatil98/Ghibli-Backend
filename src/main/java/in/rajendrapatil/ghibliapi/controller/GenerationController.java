package in.rajendrapatil.ghibliapi.controller;


import in.rajendrapatil.ghibliapi.dto.TextGenerationRequestDTO;
import in.rajendrapatil.ghibliapi.service.GhibliArtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RequiredArgsConstructor
public class GenerationController {

     private final GhibliArtService ghibliArtService;


     @PostMapping(value="/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
     public ResponseEntity<byte[]> generateGhibliArt(@RequestParam("image")MultipartFile image,
                                                     @RequestParam("prompt")String prompt){

         try {
             // Validate that image is provided
             if (image == null || image.isEmpty()) {
                 return ResponseEntity.badRequest().build();
             }

             byte[] generatedImage = ghibliArtService.createGhibliArt(image, prompt);
             return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(generatedImage);

            } catch (Exception e) {
                 e.printStackTrace();
                 return ResponseEntity.internalServerError().build();
         }
     }


     @PostMapping(value="/generate-from-text", produces = MediaType.IMAGE_PNG_VALUE)
     public ResponseEntity<byte[]> generateGhibliArtFromText(@RequestBody TextGenerationRequestDTO requestDTO){

         try {
             byte[] imageBytes = ghibliArtService.createGhibliArtFromText(requestDTO.getPrompt(), requestDTO.getStyle());
             return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);

            } catch (Exception e) {
                 e.printStackTrace();
                 return ResponseEntity.internalServerError().build();
         }
     }

}
