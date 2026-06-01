package ar.edu.um.fi.ingsr.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotebookDTO {
    @NotNull(message = "El usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;
}
