package com.meracok.resource.unit.application.service;

import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.application.dto.FiltroUsuarioDTO;
import com.meracok.application.dto.UsuarioDTO;
import com.meracok.application.service.UsuarioService;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.resource.Constants;
import com.meracok.utils.exceptions.NaoEncontradoException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioServiceTest {

    @Inject
    private UsuarioService usuarioService;

    @InjectMock
    private UsuarioRepository usuarioRepository;

    @Test
    void testBuscarPorIdComUsuarioExistente() throws NaoEncontradoException {
        Usuario usuarioExistente = criarUsuario(Constants.DEFULAT_USUARIO_ID);
        Mockito.when(usuarioRepository.findById(Mockito.any(ObjectId.class))).thenReturn(usuarioExistente);
        UsuarioDTO usuarioDTO = usuarioService.buscarPorId(new ObjectId(Constants.DEFULAT_USUARIO_ID));
        assertNotNull(usuarioDTO);
        assertEquals(Constants.DEFULAT_USUARIO_ID, usuarioDTO.getId());
    }

    @Test
    void testBuscarPorIdComUsuarioNaoExistente() {
        try {
            usuarioService.buscarPorId(new ObjectId());
        } catch (NaoEncontradoException e) {
            assertSame("Usuário não encontrado", e.getMessage());
        }
    }


    @Test
    void testBuscarPorIdComUsuarioNulo() throws NaoEncontradoException {
        Mockito.when(usuarioRepository.findById(new ObjectId())).thenReturn(null);
        assertThrows(NaoEncontradoException.class, () -> usuarioService.buscarPorId( new ObjectId()));
    }

    @Test
    void testBuscarPaginado() {
        FiltroUsuarioDTO filtro = new FiltroUsuarioDTO();
        Usuario usuario1 = criarUsuario("65bb858353626d4a39b06a88");
        Usuario usuario2 = criarUsuario("65bb858353626d4a39b06a89");
        Mockito.when(usuarioRepository.buscarPorFiltroPaginado(Mockito.any()))
                .thenReturn(Arrays.asList(usuario1, usuario2));
        List<UsuarioDTO> resultado = usuarioService.buscarPaginado(filtro);
        assertEquals(2, resultado.size());
    }

    @Test
    void testBuscarPaginadoComFiltro() {
        FiltroUsuarioDTO filtro = new FiltroUsuarioDTO();
        filtro.setNome("nome");
        Usuario usuario1 = criarUsuario("65bb858353626d4a39b06a88");
        Usuario usuario2 = criarUsuario("65bb858353626d4a39b06a89");
        Mockito.when(usuarioRepository.buscarPorFiltroPaginado(filtro)).thenReturn(Arrays.asList(usuario1, usuario2));
        List<UsuarioDTO> resultado = usuarioService.buscarPaginado(filtro);
        assertEquals(2, resultado.size());
    }

    @Test
    void testBuscarPaginadoSemResultados() {
        FiltroUsuarioDTO filtro = new FiltroUsuarioDTO();
        Mockito.when(usuarioRepository.buscarPorFiltroPaginado(filtro)).thenReturn(Arrays.asList());
        List<UsuarioDTO> resultado = usuarioService.buscarPaginado(filtro);
        assertEquals(0, resultado.size());
    }

    @Test
    void testCadastrarUsuarioComSucesso() {
        CadastroUsuarioDTO cadastroUsuarioDTO = new CadastroUsuarioDTO();
        cadastroUsuarioDTO.setNome("Nome");
        cadastroUsuarioDTO.setUsuario("usuario");
        cadastroUsuarioDTO.setSenha("senha");
        cadastroUsuarioDTO.setPerfis(new String[]{"perfil1", "perfil2"});
        Mockito.doAnswer((Answer<Void>) invocationOnMock -> {
            Usuario usuario = (Usuario) invocationOnMock.getArgument(0);
            usuario.id = new ObjectId(Constants.DEFULAT_USUARIO_ID);
            return null;
        }).when(usuarioRepository).persist(Mockito.any(Usuario.class));
        String resultado = usuarioService.cadastrar(cadastroUsuarioDTO);
        assertEquals(Constants.DEFULAT_USUARIO_ID, resultado);
    }

    @Test
    void testAtualizarSenhaComSucesso() throws NaoEncontradoException {
        ObjectId idExistente = new ObjectId();
        String novaSenha = "novaSenha";
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.id = idExistente;
        Mockito.when(usuarioRepository.findById(idExistente)).thenReturn(usuarioExistente);
        usuarioService.atualizarSenha(idExistente, novaSenha);
        Mockito.verify(usuarioRepository).update(usuarioExistente);
        assertEquals(novaSenha, usuarioExistente.getSenha());
    }

    @Test
    void testAtualizarSenhaUsuarioNaoEncontrado() {
        // Arrange
        ObjectId idNaoExistente = new ObjectId();
        String novaSenha = "novaSenha";
        Mockito.when(usuarioRepository.findById(idNaoExistente)).thenReturn(null);
        assertThrows(NaoEncontradoException.class, () -> usuarioService.atualizarSenha(idNaoExistente, novaSenha));
        Mockito.verify(usuarioRepository, Mockito.never()).update(Mockito.any(Usuario.class));
    }

    @Test
    void testHabilitarUsuarioComSucesso() throws NaoEncontradoException {
        ObjectId idExistente = new ObjectId();
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.id = idExistente;
        Mockito.when(usuarioRepository.findById(idExistente)).thenReturn(usuarioExistente);
        usuarioService.habilitarUsuario(idExistente);
        Mockito.verify(usuarioRepository).update(usuarioExistente);
        assertTrue(usuarioExistente.isHabilitado());
    }

    @Test
    void testHabilitarUsuarioNaoEncontrado() {
        ObjectId idNaoExistente = new ObjectId();
        Mockito.when(usuarioRepository.findById(idNaoExistente)).thenReturn(null);
        assertThrows(NaoEncontradoException.class, () -> usuarioService.habilitarUsuario(idNaoExistente));
        Mockito.verify(usuarioRepository, Mockito.never()).update(Mockito.any(Usuario.class));
    }

    @Test
    void testDesabilitarUsuarioComSucesso() throws NaoEncontradoException {
        ObjectId idExistente = new ObjectId();
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.id = idExistente;
        Mockito.when(usuarioRepository.findById(idExistente)).thenReturn(usuarioExistente);
        usuarioService.desabilitarUsuario(idExistente);
        Mockito.verify(usuarioRepository).update(usuarioExistente);
        assertFalse(usuarioExistente.isHabilitado());
    }

    @Test
    void testDesabilitarUsuarioNaoEncontrado() {
        ObjectId idNaoExistente = new ObjectId(Constants.DEFULAT_USUARIO_ID);
        Mockito.when(usuarioRepository.findById(idNaoExistente)).thenReturn(null);
        assertThrows(NaoEncontradoException.class, () -> usuarioService.desabilitarUsuario(idNaoExistente));
        Mockito.verify(usuarioRepository, Mockito.never()).update(Mockito.any(Usuario.class));
    }

    private static Usuario criarUsuario(String id) {
        Usuario usuario = new Usuario();
        usuario.id = new ObjectId(id);
        usuario.setNome("Usuario");
        usuario.setUsuario("usuario");
        usuario.setPerfis(new ArrayList<>(Arrays.asList("ADMIN")));
        return usuario;
    }

}
