package com.br.vr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Cartao {

    @Id
    private String numeroCartao;
    private String senha;
    private BigDecimal saldo;

    public Cartao() {
    }

    public Cartao(String numeroCartao, String senha) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
        this.saldo = new BigDecimal("500.00");
    }
}
