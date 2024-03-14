package com.meracok.resource.mongodb;

import com.meracok.domain.Usuario;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.resource.Constants;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Arrays;

@QuarkusTest
public class MongoDbDataTeste {


    public static void inserirUsuarioTesteSeNecessario(UsuarioRepository usuarioRepository) {
        if (!usuarioTesteExiste(usuarioRepository)) {
            inserirUsuarioTeste();
        }
    }

    private static boolean usuarioTesteExiste(UsuarioRepository usuarioRepository) {
        return usuarioRepository.findById(new ObjectId(Constants.DEFULAT_USUARIO_ID)) != null;
    }

    private static void inserirUsuarioTeste() {
        Usuario usuario = new Usuario();
        usuario.id = new ObjectId(Constants.DEFULAT_USUARIO_ID);
        usuario.setDataCriacao(LocalDate.now());
        usuario.setHabilitado(Constants.DEFULAT_USUARIO_HABILITADO);
        usuario.setNome(Constants.DEFULAT_USUARIO_NOME);
        usuario.setPerfis(Arrays.asList(Constants.DEFULAT_USUARIO_PERFIS));
        usuario.setSenha(Constants.DEFULAT_USUARIO_SENHA);
        usuario.setUsuario(Constants.DEFULAT_USUARIO_USUARIO);
        usuario.persist();
    }
}
