package ar.edu.um.fi.ingsr.persistence.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversationDTO {
    @NotNull(message = "El notebook es obligatorio")
    private Long notebookId;

    private String title;
}
