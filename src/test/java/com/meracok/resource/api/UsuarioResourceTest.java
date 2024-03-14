package com.meracok.resource.api;

import com.meracok.application.dto.AtualizarSenhaDTO;
import com.meracok.application.dto.AtualizarUsuarioDTO;
import com.meracok.application.dto.CadastroUsuarioDTO;
import com.meracok.domain.Usuario;
import com.meracok.infrastructure.repository.UsuarioRepository;
import com.meracok.resource.Constants;
import com.meracok.resource.mongodb.MongoDbTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource.class)
class UsuarioResourceTest {

    @Inject
    UsuarioRepository usuarioRepository;

    private static final String ID_INEXISTENTE = "05bb858353626d4a39b06a80";
    private static final String ID_INVALIDO = "65bb858353626d4a39b06a";

    @BeforeEach
    public void setup() {
        inserirUsuarioTesteSeNecessario();
    }

    private void inserirUsuarioTesteSeNecessario() {
        if (!usuarioTesteExiste()) {
            inserirUsuarioTeste();
        }
    }

    private boolean usuarioTesteExiste() {
        return usuarioRepository.findById(new ObjectId(Constants.DEFULAT_USUARIO_ID)) != null;
    }

    private void inserirUsuarioTeste() {
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

    @Test
    void testBuscarPorId() {
        given()
                .when()
                .get("/usuario/" + Constants.DEFULAT_USUARIO_ID)
                .then()
                .statusCode(200)
                .body("id", equalTo(Constants.DEFULAT_USUARIO_ID))
                .body("nome", equalTo(Constants.DEFULAT_USUARIO_NOME))
                .body("usuario", equalTo(Constants.DEFULAT_USUARIO_USUARIO))
                .body("perfis", hasItems("ADMIN"));

    }

    @Test
    void testFiltrar() {
        given()
                .when().get("usuario/filtro?habilitado=true&page=0&pageSize=10")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .log().all();
    }

    @Test
    void testCadastrar() {
        CadastroUsuarioDTO cadastroUsuarioDTO = new CadastroUsuarioDTO();
        cadastroUsuarioDTO.setNome("Usuario2");
        cadastroUsuarioDTO.setUsuario("usuario2");
        cadastroUsuarioDTO.setSenha("ASD123#");
        cadastroUsuarioDTO.setPerfis(new String[]{"ADMIN"});

        given()
                .contentType(ContentType.JSON)
                .body(cadastroUsuarioDTO)
                .when()
                .post("/usuario")
                .then()
                .statusCode(201)
                .header("location", notNullValue());
    }

    @Test
    void testAtualizar() {
        AtualizarUsuarioDTO usuarioAtualizado = new AtualizarUsuarioDTO();
        usuarioAtualizado.setId(Constants.DEFULAT_USUARIO_ID);
        usuarioAtualizado.setNome("Usuario3");
        usuarioAtualizado.setUsuario("usuario3");

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when()
                .put("/usuario")
                .then()
                .statusCode(204);// Implemente o teste para o endpoint de atualizar usuário
    }

    @Test
    void testAtualizarNorFound() {
        AtualizarUsuarioDTO usuarioAtualizado = new AtualizarUsuarioDTO();
        usuarioAtualizado.setId(ID_INEXISTENTE);
        usuarioAtualizado.setNome("Usuario3");
        usuarioAtualizado.setUsuario("usuario3");

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when()
                .put("/usuario")
                .then()
                .statusCode(404);// Implemente o teste para o endpoint de atualizar usuário
    }

    @Test
    void testAtualizarIlegalArgument() {
        AtualizarUsuarioDTO usuarioAtualizado = new AtualizarUsuarioDTO();
        usuarioAtualizado.setId(ID_INVALIDO);
        usuarioAtualizado.setNome("Usuario3");
        usuarioAtualizado.setUsuario("usuario3");

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when()
                .put("/usuario")
                .then()
                .statusCode(400);// Implemente o teste para o endpoint de atualizar usuário
    }

    @Test
    void testAtualizarSenha() {
        AtualizarSenhaDTO atualizarSenha = new AtualizarSenhaDTO();
        atualizarSenha.setId(Constants.DEFULAT_USUARIO_ID);
        atualizarSenha.setSenha("SDF234345");

        given()
                .contentType(ContentType.JSON)
                .body(atualizarSenha)
                .when()
                .put("/usuario/atualizar-senha")
                .then()
                .statusCode(204);
    }

    @Test
    void testAtualizarSenhaNotFound() {
        AtualizarSenhaDTO atualizarSenha = new AtualizarSenhaDTO();
        atualizarSenha.setId(ID_INEXISTENTE);
        atualizarSenha.setSenha("SDF234345");

        given()
                .contentType(ContentType.JSON)
                .body(atualizarSenha)
                .when()
                .put("/usuario/atualizar-senha")
                .then()
                .statusCode(404);
    }

    @Test
    void testAtualizarSenhaIllegalArguemt() {
        AtualizarSenhaDTO atualizarSenha = new AtualizarSenhaDTO();
        atualizarSenha.setId(ID_INVALIDO);
        atualizarSenha.setSenha("SDF234345");

        given()
                .contentType(ContentType.JSON)
                .body(atualizarSenha)
                .when()
                .put("/usuario/atualizar-senha")
                .then()
                .statusCode(400);
    }


    @Test
    void testDesabilitar() {
        given()
                .when()
                .put("/usuario/desabilitar/" + Constants.DEFULAT_USUARIO_ID)
                .then()
                .statusCode(204);
    }

    @Test
    void testDesabilitarNotFound() {
        given()
                .when()
                .put("/usuario/desabilitar/" + ID_INEXISTENTE)
                .then()
                .statusCode(404);
    }

    @Test
    void testDesabilitarIllegalArguemt() {
        given()
                .when()
                .put("/usuario/desabilitar/" + ID_INVALIDO)
                .then()
                .statusCode(400);
    }

    @Test
    void testHabilitar() {
        given()
                .when()
                .put("/usuario/habilitar/" + Constants.DEFULAT_USUARIO_ID)
                .then()
                .statusCode(204);
    }

    @Test
    void testHabilitarNotFound() {
        given()
                .when()
                .put("/usuario/habilitar/" + ID_INEXISTENTE)
                .then()
                .statusCode(404);
    }

    @Test
    void testHabilitarIllegalArgument() {
        given()
                .when()
                .put("/usuario/habilitar/" + ID_INVALIDO)
                .then()
                .statusCode(400);
    }
}
