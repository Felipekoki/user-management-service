package com.meracok.application.service;

import com.meracok.application.dto.AtualizarUsuarioDTO;
import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.application.dto.FiltroUsuarioDTO;
import com.meracok.application.dto.UsuarioDTO;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.converter.UsuarioConverter;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.utils.exceptions.NaoEncontradoException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public UsuarioDTO buscarPorId(ObjectId id) throws NaoEncontradoException {
        Usuario usuario = obterUsuario(id);
        return UsuarioConverter.usuarioParaUsuarioDTO(usuario);
    }

    public List<UsuarioDTO> buscarPaginado(FiltroUsuarioDTO filtro) {
        List<Usuario> usuariosFiltrados = usuarioRepository.buscarPorFiltroPaginado(filtro);
        return UsuarioConverter.listaDeUsuarioParaListaDeUsuariosDTO(usuariosFiltrados);
    }

    @Transactional
    public String cadastrar(CadastroUsuarioDTO cadastroUsuarioDTO) {
        Usuario usuario = UsuarioConverter.cadastroUsuarioDTOparaUsuario(cadastroUsuarioDTO);
        usuarioRepository.persist(usuario);
        return usuario.id.toHexString();
    }

    @Transactional
    public String atualizarUsuario(AtualizarUsuarioDTO novosDados) throws NaoEncontradoException {
        Usuario usuario = obterUsuario(new ObjectId(novosDados.getId().trim()));
        UsuarioConverter.atualizarUsuarioComAtualizarUsuarioDTO(usuario, novosDados);
        usuarioRepository.update(usuario);
        return usuario.id.toHexString();
    }

    @Transactional
    public void atualizarSenha(ObjectId id, String novaSenha) throws NaoEncontradoException {
        Usuario usuario = obterUsuario(id);
        usuario.setSenha(novaSenha);
        usuarioRepository.update(usuario);
    }

    @Transactional
    public void habilitarUsuario(ObjectId id) throws NaoEncontradoException {
        Usuario usuario = obterUsuario(id);
        usuario.setHabilitado(true);
        usuarioRepository.update(usuario);
    }

    @Transactional
    public void desabilitarUsuario(ObjectId id) throws NaoEncontradoException {
        Usuario usuario = obterUsuario(id);
        usuario.setHabilitado(false);
        usuarioRepository.update(usuario);
    }

    private Usuario obterUsuario(ObjectId codigo) throws NaoEncontradoException {
        return Optional.ofNullable(usuarioRepository.findById(codigo))
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
    }

}
