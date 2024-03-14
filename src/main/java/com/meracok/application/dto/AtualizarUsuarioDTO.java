package com.meracok.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtualizarUsuarioDTO {

    @NotBlank(message = "O parâmetro 'id' é obrigatório!")
    @Schema(type = SchemaType.STRING, nullable = false, example = "seuId")
    private String id;

    @NotBlank(message = "O parâmetro 'nome' é obrigatório!")
    @Schema(type = SchemaType.STRING, nullable = false, example = "Seu Nome")
    private String nome;

    @NotBlank(message = "O parâmetro 'usuario' é obrigatório!")
    @Pattern(regexp = "^[a-zA-Z_]+$", message = "O campo de usuário deve conter apenas letras e '_'.")
    @Schema(type = SchemaType.STRING, nullable = false, example = "seuUsuario")
    private String usuario;

    @NotNull(message = "O parâmetro 'perfis' é obrigatório!")
    @Size(min = 1, message = "Deve possuir pelo menos 1 perfil")
    @Schema(type = SchemaType.ARRAY, minItems = 1, nullable = false, example = "[\"Perfil1\", \"Perfil2\"]")
    private String[] perfis;
}
