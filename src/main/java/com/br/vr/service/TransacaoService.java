package com.br.vr.service;

import com.br.vr.dto.TransacaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransacaoService {

    @Autowired
    private CartaoService cartaoService;

    public boolean realizarTransacao(TransacaoRequest transacaoRequest) {
        return cartaoService.realizarTransacao(
                transacaoRequest.getNumeroCartao(),
                transacaoRequest.getSenhaCartao(),
                transacaoRequest.getValor()
        );
    }

    public String getMotivoFalha() {
        return cartaoService.getMotivoFalha();
    }
}
