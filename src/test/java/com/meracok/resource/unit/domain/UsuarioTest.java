package com.meracok.resource.unit.domain;

import com.meracok.domain.Usuario;
import com.meracok.resource.Constants;
import com.meracok.resource.mongodb.MongoDbTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource.class)
class UsuarioTest {

    @Test
    void testCriptografarSenha() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaOriginal");

        // Act
        usuario.criptografarSenha();

        // Assert
        assertNotNull(usuario.getSenha());
        assertNotEquals("senhaOriginal", usuario.getSenha());
    }

    @Test
    void testToString() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.id = new ObjectId(Constants.DEFULAT_USUARIO_ID);
        usuario.setNome("Teste");
        usuario.setUsuario("testeUser");
        usuario.setSenha("senha123");
        usuario.setPerfis(Arrays.asList("ADMIN", "USER"));

        // Act
        String result = usuario.toString();

        // Assert
        assertTrue(result.contains("nome='Teste'"));
        assertTrue(result.contains("usuario='testeUser'"));
        assertTrue(result.contains("senha='senha123'"));
        assertTrue(result.contains("perfis=[ADMIN, USER]"));
        assertTrue(result.contains("habilitado=true"));
        assertTrue(result.contains("id="+Constants.DEFULAT_USUARIO_ID));
    }

    @Test
    void testPersist() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setUsuario("testeUser");
        usuario.setSenha("senha123");
        usuario.setPerfis(Arrays.asList("ADMIN", "USER"));

        usuario.persist();
        assertFalse(usuario.id.toHexString().isEmpty());
    }

    @Test
    void testFindById() {
        Usuario usuario = Usuario.findById(new ObjectId(Constants.DEFULAT_USUARIO_ID));
        assertEquals("Usuario3", usuario.getNome());
        assertEquals("usuario3", usuario.getUsuario());
        assertEquals("SDF234345", usuario.getSenha());
        assertFalse(usuario.getPerfis().isEmpty());
    }
}

