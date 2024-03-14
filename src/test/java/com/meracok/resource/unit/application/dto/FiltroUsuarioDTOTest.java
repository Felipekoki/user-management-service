package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.FiltroUsuarioDTO;
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
class FiltroUsuarioDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void criarFiltroUsuarioDTOComSucesso() {
        // Arrange
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String perfil = "Perfil1";
        Boolean habilitado = true;
        int page = 1;
        int pageSize = 10;

        // Act
        FiltroUsuarioDTO dto = new FiltroUsuarioDTO(nome, usuario, perfil, habilitado, page, pageSize);
        Set<ConstraintViolation<FiltroUsuarioDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(nome, dto.getNome());
        assertEquals(usuario, dto.getUsuario());
        assertEquals(perfil, dto.getPerfil());
        assertEquals(habilitado, dto.getHabilitado());
        assertEquals(page, dto.getPage());
        assertEquals(pageSize, dto.getPageSize());
        assertTrue(violations.isEmpty());
    }

    @Test
    void criarFiltroUsuarioDTOComCamposHabilitadoNulo() {
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String perfil = "Perfil1";
        Boolean habilitado = null;
        int page = 0;
        int pageSize = 10;

        FiltroUsuarioDTO dto = new FiltroUsuarioDTO(nome, usuario, perfil, habilitado, page, pageSize);
        Set<ConstraintViolation<FiltroUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("A habilitação não pode ser nula", violations.iterator().next().getMessage());
    }

    @Test
    void criarFiltroUsuarioDTOComCamposPageEPageSizeNegativo() {
        String nome = "Seu Nome";
        String usuario = "SeuUsuario";
        String perfil = "Perfil1";
        Boolean habilitado = true;
        int page = -1;
        int pageSize = -10;

        FiltroUsuarioDTO dto = new FiltroUsuarioDTO(nome, usuario, perfil, habilitado, page, pageSize);
        Set<ConstraintViolation<FiltroUsuarioDTO>> violations = validator.validate(dto);
        Iterator<ConstraintViolation<FiltroUsuarioDTO>> iterator = violations.iterator();
        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertTrue(errorMessages.contains("O tamanho da página deve ser maior ou igual a 1"));
        assertTrue(errorMessages.contains("A página deve ser maior ou igual a 0"));
    }

    @Test
    void converterParaMapComTodosParametrosPreenchidos() {
        FiltroUsuarioDTO filtroUsuarioDTO = new FiltroUsuarioDTO("Seu Nome", "SeuUsuario", "Perfil1", true, 1, 10);
        var mapa = filtroUsuarioDTO.converterParaMap();

        assertEquals(4, mapa.size());
        assertTrue(mapa.containsKey("nome like ?1"));
        assertTrue(mapa.containsKey("usuario like ?1"));
        assertTrue(mapa.containsKey("perfil like ?1"));
        assertTrue(mapa.containsKey("habilitado"));
    }

    @Test
    void converterParaMapComParametrosNulos() {
        FiltroUsuarioDTO filtroUsuarioDTO = new FiltroUsuarioDTO(null, null, null, null, 0, 0);
        var mapa = filtroUsuarioDTO.converterParaMap();
        assertEquals(1, mapa.size());
        assertFalse(mapa.containsKey("nome like ?1"));
        assertFalse(mapa.containsKey("usuario like ?1"));
        assertFalse(mapa.containsKey("perfil like ?1"));
        assertTrue(mapa.containsKey("habilitado"));
        assertTrue((Boolean) mapa.get("habilitado"));
    }

    @Test
    void converterParaMapComHabilitadoFalso() {
        FiltroUsuarioDTO filtroUsuarioDTO = new FiltroUsuarioDTO("Seu Nome", "SeuUsuario", "Perfil1", false, 1, 10);
        var mapa = filtroUsuarioDTO.converterParaMap();
        assertEquals(4, mapa.size());
        assertTrue(mapa.containsKey("nome like ?1"));
        assertTrue(mapa.containsKey("usuario like ?1"));
        assertTrue(mapa.containsKey("perfil like ?1"));
        assertTrue(mapa.containsKey("habilitado"));
        assertFalse((Boolean) mapa.get("habilitado"));
    }

    @Test
    void converterParaMapComPageSizeZero() {
        FiltroUsuarioDTO dto = new FiltroUsuarioDTO("Seu Nome", "SeuUsuario", "Perfil1", true, 1, 0);
        var mapa = dto.converterParaMap();
        assertEquals(4, mapa.size());
        assertTrue(mapa.containsKey("nome like ?1"));
        assertTrue(mapa.containsKey("usuario like ?1"));
        assertTrue(mapa.containsKey("perfil like ?1"));
        assertTrue(mapa.containsKey("habilitado"));

        Set<ConstraintViolation<FiltroUsuarioDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("O tamanho da página deve ser maior ou igual a 1", violations.iterator().next().getMessage());
    }

    @Test
    void converterParaMapComPageSizeNegativo() {
        FiltroUsuarioDTO filtroUsuarioDTO = new FiltroUsuarioDTO("Seu Nome", "SeuUsuario", "Perfil1", true, 1, -5);
        var mapa = filtroUsuarioDTO.converterParaMap();
        assertEquals(4, mapa.size());
        assertTrue(mapa.containsKey("nome like ?1"));
        assertTrue(mapa.containsKey("usuario like ?1"));
        assertTrue(mapa.containsKey("perfil like ?1"));
        assertTrue(mapa.containsKey("habilitado"));
    }
}

