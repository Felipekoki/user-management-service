package com.meracok.infrastructure.rest;

import com.meracok.application.dto.*;
import com.meracok.application.service.UsuarioService;
import com.meracok.utils.exceptions.NaoEncontradoException;
import com.meracok.utils.resource.ApiResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Usuario", description = "Endpoints relacionados ao cadastro de usuários")
@ApplicationScoped
public class UsuarioResrouce {

    @Inject
    UsuarioService usuarioService;

    private static final String ERRO_REQUISICAO = "Erro na Requisisção";

    @Operation(description = "Recupera um usuário pelo ID", operationId = "UsuarioResource#buscarPorId", summary = "Recupera um usuário pelo ID")
    @APIResponse(name = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class, type = SchemaType.OBJECT)), description = "Consulta de usuário realizada com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @GET
    @Path("{id}")
    public Response buscarPorId(@PathParam("id") final String id) {
        try {
            return ApiResponse.ok(usuarioService.buscarPorId(new ObjectId(id.trim())));
        } catch (IllegalArgumentException illegalArgumentException){
            return ApiResponse.badRequest(new ErrorMessage(ERRO_REQUISICAO, illegalArgumentException.getMessage()));
        } catch (NaoEncontradoException naoEncontradoException){
            return ApiResponse.notFound(naoEncontradoException.getMessage());
        }
    }

    @Operation(description = "Filtrar Usuários", operationId = "UsuarioResource#filtrar", summary = "Recupera usuários filtrados")
    @APIResponse(name = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class, type = SchemaType.ARRAY)), description = "Consulta de usuários realizada com sucesso")
    @APIResponse(responseCode = "404", description = "Nenhum usuário encontrado com o filtro especificado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @GET
    @Path("filtro")
    public Response filtrar(@BeanParam FiltroUsuarioDTO filtro) {
        return ApiResponse.ok(usuarioService.buscarPaginado(filtro));
    }

    @Operation(description = "Cadastrar um novo usuário", operationId = "UsuarioResource#cadastrar", summary = "Cadastra um novo usuário")
    @APIResponse(name = "CREATED", responseCode = "201", description = "Usuário cadastrado com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @POST
    @Transactional
    public Response cadastrar(final CadastroUsuarioDTO usuario, @Context UriInfo uriInfo){
        String idUsuario = usuarioService.cadastrar(usuario);
        return ApiResponse.created(idUsuario, uriInfo);
    }

    @Operation(description = "Atualizar um usuário", operationId = "UsuarioResource#atualizar", summary = "Atualiza um usuário")
    @APIResponse(name = "NOCONTENT", responseCode = "204", description = "Usuário atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @PUT
    @Transactional
    public Response atualizar(final AtualizarUsuarioDTO usuario) {
        try {
            usuarioService.atualizarUsuario(usuario);
            return ApiResponse.noContent();
        } catch (NaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (IllegalArgumentException illegalArgumentException){
            return ApiResponse.badRequest(new ErrorMessage(ERRO_REQUISICAO, illegalArgumentException.getMessage()));
        }
    }

    @Operation(description = "Atualizar a senha de um usuáiro", operationId = "UsuarioResource#atualizarSenha", summary = "Atualiza senha de um usuário")
    @APIResponse(name = "NOCONTENT", responseCode = "204", description = "Usuário atualizado com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @PUT
    @Path("/atualizar-senha")
    @Transactional
    public Response atualizarSenha(final AtualizarSenhaDTO atualizarSenhaDTO) {
        try {
            usuarioService.atualizarSenha(new ObjectId(atualizarSenhaDTO.getId()), atualizarSenhaDTO.getSenha());
            return ApiResponse.noContent();
        } catch (NaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (IllegalArgumentException illegalArgumentException){
            return ApiResponse.badRequest(new ErrorMessage(ERRO_REQUISICAO, illegalArgumentException.getMessage()));
        }
    }

    @Operation(description = "Desabilitar um usuário", operationId = "UsuarioResource#desabilitar", summary = "Desabilita um usuário")
    @APIResponse(name = "NOCONTENT", responseCode = "204", description = "Usuário desabilitado com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @PUT
    @Path("desabilitar/{id}")
    @Transactional
    public Response desabilitar(@PathParam("id") final String id) {
        try {
            usuarioService.desabilitarUsuario(new ObjectId(id.trim()));
            return ApiResponse.noContent();
        } catch (NaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (IllegalArgumentException illegalArgumentException){
            return ApiResponse.badRequest(new ErrorMessage(ERRO_REQUISICAO, illegalArgumentException.getMessage()));
        }
    }

    @Operation(description = "Habilitar um usuário", operationId = "UsuarioResource#habilitar", summary = "Habilita um usuário")
    @APIResponse(name = "NOCONTENT", responseCode = "204", description = "Usuário habilitado com sucesso")
    @APIResponse(responseCode = "400", description = "Requisição inválida")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    @APIResponse(responseCode = "500", description = "Erro interno no servidor")
    @PUT
    @Path("habilitar/{id}")
    @Transactional
    public Response habilitar(@PathParam("id") final String id) {
        try {
            usuarioService.habilitarUsuario(new ObjectId(id.trim()));
            return ApiResponse.noContent();
        } catch (NaoEncontradoException e) {
            return ApiResponse.notFound(e.getMessage());
        } catch (IllegalArgumentException illegalArgumentException){
            return ApiResponse.badRequest(new ErrorMessage(ERRO_REQUISICAO, illegalArgumentException.getMessage()));
        }
    }
}
