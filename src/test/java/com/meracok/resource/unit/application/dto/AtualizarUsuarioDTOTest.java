package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.AtualizarUsuarioDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AtualizarUsuarioDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void criarAtualizarUsuarioDTOComSucesso() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(nome, dto.getNome());
        assertEquals(usuario, dto.getUsuario());
        assertArrayEquals(perfis, dto.getPerfis());
        assertTrue(violations.isEmpty());
    }

    @Test
    void criarAtualizarUsuarioDTOComIdNuloDeveFalhar() {
        String id = null;
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'id' é obrigatório!", violations.iterator().next().getMessage());
    }

    @Test
    void criarAtualizarUsuarioDTOComNomeNuloDeveFalhar() {
        // Arrange
        String id = "seuId";
        String nome = null;
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'nome' é obrigatório!", violations.iterator().next().getMessage());
    }

    @Test
    void criarAtualizarUsuarioDTOComUsuarioVazioDeveFalhar() {
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "";
        String[] perfis = {"Perfil1", "Perfil2"};

        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);
        Iterator<ConstraintViolation<AtualizarUsuarioDTO>> iterator = violations.iterator();

        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertTrue(errorMessages.contains("O parâmetro 'usuario' é obrigatório!"));
        assertTrue(errorMessages.contains("O campo de usuário deve conter apenas letras e '_'."));

        assertEquals(2, errorMessages.size());
    }

    @Test
    void criarAtualizarUsuarioDTOComPerfisVazioDeveFalhar() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {};

        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Deve possuir pelo menos 1 perfil", violations.iterator().next().getMessage());
    }

    @Test
    void criarAtualizarUsuarioDTOComPerfisNuloDeveFalhar() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = null;

        AtualizarUsuarioDTO dto = new AtualizarUsuarioDTO(id, nome, usuario, perfis);
        Set<ConstraintViolation<AtualizarUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'perfis' é obrigatório!", violations.iterator().next().getMessage());
    }
}

