package in.rajendrapatil.ghibliapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextGenerationRequestDTO {

    private String prompt;
    private String style;
}
