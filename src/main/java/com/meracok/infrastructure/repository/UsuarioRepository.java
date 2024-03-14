package com.meracok.infrastructure.repository;

import com.meracok.domain.Usuario;
import com.meracok.application.dto.FiltroUsuarioDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UsuarioRepository implements PanacheMongoRepositoryBase<Usuario, ObjectId> {

    public List<Usuario> buscarPorFiltroPaginado(FiltroUsuarioDTO filtroDTO) {
        return find(criarFiltroDocument(filtroDTO.converterParaMap()))
                .page(Page.of(filtroDTO.getPage(), filtroDTO.getPageSize()))
                .list();
    }

    private Document criarFiltroDocument(Map<String, ?> filtroMapeado) {
        Document filtro = new Document();
        filtroMapeado.forEach((chave, valor) -> adicionarAoFiltro(filtro, chave, valor));
        return filtro;
    }

    private void adicionarAoFiltro(Document filtro, String chave, Object valor) {
        Object valorValidado = (valor instanceof String valorEmString) ? converterParaExpressaoLike(valorEmString) : valor;
        filtro.append(chave, valorValidado);
    }

    private String converterParaExpressaoLike(@org.jetbrains.annotations.NotNull String filtro) {
        return String.format("^%s.*$", filtro.toLowerCase());
    }



}
