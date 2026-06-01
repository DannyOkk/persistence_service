package ar.edu.um.fi.ingsr.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentDTO {
    @NotNull(message = "El usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El nombre de archivo es obligatorio")
    private String filename;

    @NotBlank(message = "El path de archivo es obligatorio")
    private String filePath;

    private String jobId;
    private String status;
    private String extractedText;
    private Long notebookId;
}
