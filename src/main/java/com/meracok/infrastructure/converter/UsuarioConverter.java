package com.meracok.infrastructure.converter;

import com.meracok.application.dto.AtualizarUsuarioDTO;
import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.application.dto.UsuarioDTO;
import com.meracok.domain.Usuario;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioConverter {

    public static Usuario cadastroUsuarioDTOparaUsuario(CadastroUsuarioDTO cadastroDTO) {
        Usuario usuario = new Usuario(cadastroDTO.getNome(), cadastroDTO.getUsuario(), cadastroDTO.getSenha(),
        LocalDate.now(), Arrays.stream(cadastroDTO.getPerfis()).toList(), true);
        usuario.criptografarSenha();
        return usuario;
    }

    public static void atualizarUsuarioComAtualizarUsuarioDTO(Usuario usuario, AtualizarUsuarioDTO novoDados) {
        usuario.setNome(Objects.requireNonNullElse(novoDados.getNome(), usuario.getNome()));
        usuario.setUsuario(Objects.requireNonNullElse(novoDados.getUsuario(), usuario.getUsuario()));
        if (novoDados.getPerfis() != null) {
            usuario.setPerfis(Arrays.asList(novoDados.getPerfis()));
        }
    }

    public static List<UsuarioDTO> listaDeUsuarioParaListaDeUsuariosDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioConverter::usuarioParaUsuarioDTO).toList();
    }

    public static UsuarioDTO usuarioParaUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.id.toHexString())
                .usuario(usuario.getUsuario())
                .nome(usuario.getNome())
                .perfis(usuario.getPerfis().toArray(String[]::new))
                .build();
    }
}
