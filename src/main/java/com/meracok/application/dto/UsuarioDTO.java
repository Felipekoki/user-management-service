package com.meracok.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {
    private String id;
    private String nome;
    private String usuario;
    private String[] perfis;
}
