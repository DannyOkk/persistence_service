package ar.edu.um.fi.ingsr.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SummaryDTO {
    @NotNull(message = "El documento es obligatorio")
    private Integer documentId;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;

    private String modelUsed;
}
