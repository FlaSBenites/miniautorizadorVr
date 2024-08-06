package com.br.vr.controller;

import com.br.vr.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoRequest transacaoRequest) {
        boolean transacaoAutorizada = cartaoService.realizarTransacao(
                transacaoRequest.getNumeroCartao(),
                transacaoRequest.getSenhaCartao(),
                transacaoRequest.getValor()
        );

        if (transacaoAutorizada) {
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoService.getMotivoFalha());
        }
    }
}

class TransacaoRequest {
    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;

    // Getters and setters
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getSenhaCartao() {
        return senhaCartao;
    }

    public void setSenhaCartao(String senhaCartao) {
        this.senhaCartao = senhaCartao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
