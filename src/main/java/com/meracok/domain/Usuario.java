package com.meracok.domain;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario extends PanacheMongoEntity {

    private String nome;
    private String usuario;
    private String senha;
    private LocalDate dataCriacao = LocalDate.now();
    private List<String> perfis;
    private boolean habilitado = true;

    public void criptografarSenha(){
        senha = BcryptUtil.bcryptHash(senha);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", usuario='" + usuario + '\'' +
                ", senha='" + senha + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", perfis=" + perfis +
                ", habilitado=" + habilitado +
                ", id=" + id +
                '}';
    }
}
