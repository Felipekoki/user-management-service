package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.ErrorMessage;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ErrorMessageTest {

    @Test
    void criarErrorMessageSucesso(){
        String titulo = "titulo";
        String mensagem = "mensagem";
        ErrorMessage errorMessage = new ErrorMessage(titulo, mensagem);
        errorMessage.setTitulo("TituloModificado");
        errorMessage.setMensagem("MensagemModificada");
        // Assert
        assertEquals("TituloModificado", errorMessage.getTitulo());
        assertEquals("MensagemModificada", errorMessage.getMensagem());

    }
}
