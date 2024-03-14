package com.meracok.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AtualizarSenhaDTO {
    @NotBlank(message = "O parâmetro 'id' é obrigatório!" )
    private String id;

    @NotBlank(message = "O parâmetro 'senha' é obrigatório!" )
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve conter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais."
    )
    private String senha;
}
