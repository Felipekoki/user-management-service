package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.AtualizarSenhaDTO;
import com.meracok.resource.Constants;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class AtualizarSenhaDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testAtualizarSenhaDTOValido() {
        AtualizarSenhaDTO dto = new AtualizarSenhaDTO(Constants.DEFULAT_USUARIO_ID, "Senha@123");
        Set<ConstraintViolation<AtualizarSenhaDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAtualizarSenhaDTOComIdNulo() {
        AtualizarSenhaDTO dto = new AtualizarSenhaDTO(null, "Senha@123");
        Set<ConstraintViolation<AtualizarSenhaDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'id' é obrigatório!", violations.iterator().next().getMessage());
    }

    @Test
    void testAtualizarSenhaDTOComSenhaInvalida() {
        AtualizarSenhaDTO dto = new AtualizarSenhaDTO(Constants.DEFULAT_USUARIO_ID, "senhafraca");
        Set<ConstraintViolation<AtualizarSenhaDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("A senha deve conter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais.", violations.iterator().next().getMessage());
    }
}
