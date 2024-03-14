package com.meracok.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class    FiltroUsuarioDTO {
    @QueryParam("nome")
    private String nome;

    @QueryParam("usuario")
    private String usuario;

    @QueryParam("perfil")
    private String perfil;

    @NotNull(message = "A habilitação não pode ser nula")
    @QueryParam("habilitado")
    @Schema(defaultValue = "true", type = SchemaType.BOOLEAN)
    private Boolean habilitado = true;

    @NotNull(message = "A página não pode ser nula")
    @Min(value = 0, message = "A página deve ser maior ou igual a 0")
    @QueryParam("page")
    @Schema(defaultValue = "0", type = SchemaType.INTEGER)
    private int page = 1;

    @NotNull(message = "O tamanho da página não pode ser nulo")
    @Min(value = 1, message = "O tamanho da página deve ser maior ou igual a 1")
    @QueryParam("pageSize")
    @Schema(defaultValue = "10", type = SchemaType.INTEGER)
    private int pageSize = 10;

    public Map<String, Object> converterParaMap() {
        return Stream.of(
                condicao("nome like ?1", Optional.ofNullable(nome).orElse("")),
                condicao("usuario like ?1", Optional.ofNullable(usuario).orElse("")),
                condicao("perfil like ?1", Optional.ofNullable(perfil).orElse("")),
                condicao("habilitado", Optional.ofNullable(habilitado).orElse(true))
        ).filter(entry -> !entry.getValue().equals("")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    private <T> Map.Entry<String, T> condicao(String chave, T valor) {
        return Map.entry(chave, valor);
    }

}
