package com.meracok.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {
    private String titulo;
    private String mensagem;
}
