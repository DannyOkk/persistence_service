package ar.edu.um.fi.ingsr.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDTO {
    @NotNull(message = "La conversacion es obligatoria")
    private Long conversationId;

    @NotBlank(message = "El rol es obligatorio")
    private String role;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;
}
