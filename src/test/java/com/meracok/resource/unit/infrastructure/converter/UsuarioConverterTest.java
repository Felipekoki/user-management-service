package com.meracok.resource.unit.infrastructure.converter;

import com.meracok.application.dto.AtualizarUsuarioDTO;
import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.application.dto.UsuarioDTO;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.converter.UsuarioConverter;
import com.meracok.resource.Constants;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioConverterTest {

    @Test
    void criarUsuarioAPartirDeCadastroDTOTest() {
        // Arrange
        CadastroUsuarioDTO cadastroDTO = new CadastroUsuarioDTO("Usuario", "usuario", "senha", new String[]{"ADMIN"});
        Usuario usuairo = UsuarioConverter.cadastroUsuarioDTOparaUsuario(cadastroDTO);

        // Assert
        assertNotNull(usuairo);
        assertEquals("Usuario", usuairo.getNome());
        assertEquals("usuario", usuairo.getUsuario());
    }

    @Test
    void atualizarDadosDoUsuarioTest() {
        // Arrange
        AtualizarUsuarioDTO atualizarUsuarioDTO = new AtualizarUsuarioDTO( Constants.DEFULAT_USUARIO_ID, "Usuario Atualizado", "usuarioatualizado", new String[]{"USER"});
        Usuario usuario = criarUsuario();

        // Act
        UsuarioConverter.atualizarUsuarioComAtualizarUsuarioDTO(usuario, atualizarUsuarioDTO);

        // Assert
        assertNotNull(usuario);
        assertEquals("Usuario Atualizado", usuario.getNome());
        assertEquals("usuarioatualizado", usuario.getUsuario());

    }

    @Test
    void listaDeUsuarioParaListaDeUsuariosDTOTest(){
        List<Usuario> usuairos = new ArrayList<>(Arrays.asList(criarUsuario()));

        List<UsuarioDTO> usuariosDTO = UsuarioConverter.listaDeUsuarioParaListaDeUsuariosDTO(usuairos);

        assertNotNull(usuariosDTO);
        assertEquals(usuairos.get(0).getNome(), usuariosDTO.get(0).getNome());

    }

    @Test
    void usuarioParaUsuarioDTOTest(){
        Usuario usuario = criarUsuario();
        UsuarioDTO usuarioDTO = UsuarioConverter.usuarioParaUsuarioDTO(usuario);

        assertNotNull(usuarioDTO);
        assertEquals(usuarioDTO.getUsuario(), usuario.getUsuario());
        assertFalse(Arrays.stream(usuarioDTO.getPerfis()).toList().isEmpty());
    }

    private Usuario criarUsuario(){
        Usuario usuario = new Usuario();
        usuario.id = new ObjectId(Constants.DEFULAT_USUARIO_ID);
        usuario.setNome("Usuario");
        usuario.setUsuario("usuario");
        usuario.setPerfis(new ArrayList<>(Arrays.asList("ADMIN")));
        return usuario;
    }
}
