package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.CadastroUsuarioDTO;
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
class CadastroUsuarioDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void criarCadastroUsuarioDTOComSucesso() {
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String senha = "A123456#";
        String[] perfis = {"Perfil1", "Perfil2"};

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(nome, dto.getNome());
        assertEquals(usuario, dto.getUsuario());
        assertEquals(senha, dto.getSenha());
        assertArrayEquals(perfis, dto.getPerfis());
        assertFalse(violations.isEmpty());
    }

    @Test
    void criarCadastroUsuarioDTOComNomeNuloDeveFalhar() {
        // Arrange
        String nome = null;
        String usuario = "SeuUsuario";
        String senha = "Senha@123";
        String[] perfis = {"Perfil1", "Perfil2"};

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);

        Iterator<ConstraintViolation<CadastroUsuarioDTO>> iterator = violations.iterator();

        String erroParametroObrigatorio = iterator.next().getMessage();

        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'nome' é obrigatório!", erroParametroObrigatorio);
    }

    @Test
    void criarCadastroUsuarioDTOComUsuarioVazioDeveFalhar() {
        // Arrange
        String nome = "Seu Nome";
        String usuario = "";
        String senha = "Senha@123";
        String[] perfis = {"Perfil1", "Perfil2"};

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);
        Iterator<ConstraintViolation<CadastroUsuarioDTO>> iterator = violations.iterator();

        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertTrue(errorMessages.contains("O parâmetro 'usuario' é obrigatório!"));
        assertTrue(errorMessages.contains("O campo de usuário deve conter apenas letras e '_'."));

        assertEquals(2, errorMessages.size());
    }

    @Test
    void criarCadastroUsuarioDTOComSenhaInvalidaDeveFalhar() {
        // Arrange
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String senha = "senhaFraca";
        String[] perfis = {"Perfil1", "Perfil2"};

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("A senha deve conter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais.", violations.iterator().next().getMessage());
    }

    @Test
    void criarCadastroUsuarioDTOComPerfisVazioDeveFalhar() {
        // Arrange
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String senha = "Senha@123";
        String[] perfis = {};

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);
        Iterator<ConstraintViolation<CadastroUsuarioDTO>> iterator = violations.iterator();

        String erroParametroObrigatorio = iterator.next().getMessage();

        assertEquals(1, violations.size());
        assertEquals("Deve possuir pelo menos 1 perfil", erroParametroObrigatorio);
    }

    @Test
    void criarCadastroUsuarioDTOComPerfisNuloDeveFalhar() {
        // Arrange
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String senha = "Senha@123";
        String[] perfis = null;

        CadastroUsuarioDTO dto = new CadastroUsuarioDTO(nome, usuario, senha, perfis);
        Set<ConstraintViolation<CadastroUsuarioDTO>> violations = validator.validate(dto);
        Iterator<ConstraintViolation<CadastroUsuarioDTO>> iterator = violations.iterator();

        String erroParametroObrigatorio = iterator.next().getMessage();

        assertEquals(1, violations.size());
        assertEquals("O parâmetro 'perfis' é obrigatório!", erroParametroObrigatorio);
    }
}

