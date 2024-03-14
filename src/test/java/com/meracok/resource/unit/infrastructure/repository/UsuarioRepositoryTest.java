package com.meracok.resource.unit.infrastructure.repository;

import com.meracok.application.dto.FiltroUsuarioDTO;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.resource.mongodb.MongoDbTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource.class)
class UsuarioRepositoryTest {
    @Inject
    UsuarioRepository usuarioRepository;

    @Test
    void buscarPorFiltroPaginado_FiltroVazioTest() {
        FiltroUsuarioDTO filtroDTO = new FiltroUsuarioDTO();
        List<Usuario> usuarios = usuarioRepository.buscarPorFiltroPaginado(filtroDTO);
        assertTrue(usuarios.isEmpty());
    }

    @Test
    void buscarPorFiltroPaginado_FiltroPadraoTest() {
        FiltroUsuarioDTO filtroDTO = new FiltroUsuarioDTO();
        filtroDTO.setHabilitado(true);
        filtroDTO.setPage(0);
        filtroDTO.setPageSize(10);

        List<Usuario> usuarios = usuarioRepository.buscarPorFiltroPaginado(filtroDTO);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void buscarPorFiltroPaginado_FiltroCompletoTest() {
        FiltroUsuarioDTO filtroDTO = new FiltroUsuarioDTO();
        filtroDTO.setNome("Usuario");
        filtroDTO.setUsuario("usuario");
        filtroDTO.setPerfil("ADMIN");
        filtroDTO.setHabilitado(true);
        filtroDTO.setPage(0);
        filtroDTO.setPageSize(10);

        List<Usuario> usuarios = usuarioRepository.buscarPorFiltroPaginado(filtroDTO);
        assertTrue(usuarios.isEmpty());
    }
}
