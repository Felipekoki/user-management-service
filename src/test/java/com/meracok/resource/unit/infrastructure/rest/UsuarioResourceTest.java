package com.meracok.resource.unit.infrastructure.rest;

import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.application.dto.FiltroUsuarioDTO;
import com.meracok.application.dto.UsuarioDTO;
import com.meracok.application.service.UsuarioService;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.converter.UsuarioConverter;
import com.meracok.infrastructure.rest.UsuarioResrouce;
import com.meracok.resource.Constants;
import com.meracok.utils.exceptions.NaoEncontradoException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioResourceTest {

    @Inject
    UsuarioResrouce usuarioResource;

    @InjectMock
    UsuarioService usuarioService;

    @Test
    void TestBuscarPorIdSucesso() throws NaoEncontradoException {
        Mockito.when(usuarioService.buscarPorId(Mockito.any(ObjectId.class))).thenReturn(criarUsuarioDTO());
        Response response = usuarioResource.buscarPorId(Constants.DEFULAT_USUARIO_ID);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testBuscarPorIdErroIllegalArgumentException() throws NaoEncontradoException {
        Mockito.when(usuarioService.buscarPorId(Mockito.any(ObjectId.class))).thenThrow(new IllegalArgumentException("Mensagem de erro"));
        Response response = usuarioResource.buscarPorId("invalidObjectId");
        assertEquals(400, response.getStatus());
    }

    @Test
    void testBuscarPorIdErroNaoEncontradoException() throws NaoEncontradoException {
        Mockito.when(usuarioService.buscarPorId(Mockito.any(ObjectId.class)))
                .thenThrow(new NaoEncontradoException("Mensagem de erro"));
        Response response = usuarioResource.buscarPorId(Constants.DEFULAT_USUARIO_ID);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testFiltrarDadosNulos() {
        FiltroUsuarioDTO filtro = new FiltroUsuarioDTO();
        Mockito.when(usuarioService.buscarPaginado(filtro)).thenReturn(Mockito.any(List.class));
        Response response = usuarioResource.filtrar(filtro);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testFiltrarSucesso() {
        FiltroUsuarioDTO filtro = new FiltroUsuarioDTO();
        filtro.setHabilitado(true);
        filtro.setPage(0);
        filtro.setPageSize(10);
        Mockito.when(usuarioService.buscarPaginado(filtro)).thenReturn(Mockito.any(List.class));
        Response response = usuarioResource.filtrar(filtro);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testCadastrarSucesso() {
        UriInfo uriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(UriBuilder.fromPath("http://example.com/path"));
        Mockito.when(usuarioService.cadastrar(Mockito.any(CadastroUsuarioDTO.class))).thenReturn("idGerado");
        CadastroUsuarioDTO usuarioDTO = new CadastroUsuarioDTO(); // Preencha com os dados do usuário
        Response response = usuarioResource.cadastrar(usuarioDTO, uriInfo);
        assertEquals(201, response.getStatus());
        assertNull(response.getEntity());
    }

    private static Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.id = new ObjectId(Constants.DEFULAT_USUARIO_ID);
        usuario.setNome("Usuario");
        usuario.setUsuario("usuario");
        usuario.setPerfis(new ArrayList<>(Arrays.asList("ADMIN")));
        return usuario;
    }

    private static UsuarioDTO criarUsuarioDTO() {
        return UsuarioConverter.usuarioParaUsuarioDTO(criarUsuario());
    }
}
