package com.meracok.resource.unit.application.dto;

import com.meracok.application.dto.UsuarioDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioDTOTest {

    @Test
    void criarUsuarioDTOComSucesso() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertEquals(id, usuarioDTO.getId());
        assertEquals(nome, usuarioDTO.getNome());
        assertEquals(usuario, usuarioDTO.getUsuario());
        assertArrayEquals(perfis, usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComNoArgsConstructorSuccess() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(id);
        usuarioDTO.setNome(nome);
        usuarioDTO.setUsuario(usuario);
        usuarioDTO.setPerfis(perfis);

        // Assert
        assertEquals(id, usuarioDTO.getId());
        assertEquals(nome, usuarioDTO.getNome());
        assertEquals(usuario, usuarioDTO.getUsuario());
        assertArrayEquals(perfis, usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComPerfisNulo() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = null;

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertEquals(id, usuarioDTO.getId());
        assertEquals(nome, usuarioDTO.getNome());
        assertEquals(usuario, usuarioDTO.getUsuario());
        assertNull(usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComUsuarioNulo() {
        // Arrange
        String id = "seuId";
        String nome = "Seu Nome";
        String usuario = null;
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertEquals(id, usuarioDTO.getId());
        assertEquals(nome, usuarioDTO.getNome());
        assertNull(usuarioDTO.getUsuario());
        assertArrayEquals(perfis, usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComNomeNulo() {
        // Arrange
        String id = "seuId";
        String nome = null;
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertEquals(id, usuarioDTO.getId());
        assertNull(usuarioDTO.getNome());
        assertEquals(usuario, usuarioDTO.getUsuario());
        assertArrayEquals(perfis, usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComIdNulo() {
        // Arrange
        String id = null;
        String nome = "Seu Nome";
        String usuario = "seuUsuario";
        String[] perfis = {"Perfil1", "Perfil2"};

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertNull(usuarioDTO.getId());
        assertEquals(nome, usuarioDTO.getNome());
        assertEquals(usuario, usuarioDTO.getUsuario());
        assertArrayEquals(perfis, usuarioDTO.getPerfis());
    }

    @Test
    void criarUsuarioDTOComTodosCamposNulos() {
        // Arrange
        String id = null;
        String nome = null;
        String usuario = null;
        String[] perfis = null;

        // Act
        UsuarioDTO usuarioDTO = new UsuarioDTO(id, nome, usuario, perfis);

        // Assert
        assertNull(usuarioDTO.getId());
        assertNull(usuarioDTO.getNome());
        assertNull(usuarioDTO.getUsuario());
        assertNull(usuarioDTO.getPerfis());
    }
}

