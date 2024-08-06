package com.br.vr.service;

import com.br.vr.model.Cartao;
import com.br.vr.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    private String motivoFalha;

    public Cartao criarCartao(String numeroCartao, String senha) {
        Cartao cartao = new Cartao(numeroCartao, senha);
        cartao.setSaldo(new BigDecimal("500.00"));
        return cartaoRepository.save(cartao);
    }

    public Optional<Cartao> obterSaldo(String numeroCartao) {
        return cartaoRepository.findById(numeroCartao);
    }

    public boolean realizarTransacao(String numeroCartao, String senhaCartao, BigDecimal valor) {
        Optional<Cartao> optionalCartao = cartaoRepository.findById(numeroCartao);
        if (!optionalCartao.isPresent()) {
            motivoFalha = "CARTAO_INEXISTENTE";
            return false;
        }

        Cartao cartao = optionalCartao.get();
        if (!cartao.getSenha().equals(senhaCartao)) {
            motivoFalha = "SENHA_INVALIDA";
            return false;
        }

        if (cartao.getSaldo().compareTo(valor) < 0) {
            motivoFalha = "SALDO_INSUFICIENTE";
            return false;
        }

        cartao.setSaldo(cartao.getSaldo().subtract(valor));
        cartaoRepository.save(cartao);
        return true;
    }

    public boolean existeCartao(String numeroCartao) {
        return cartaoRepository.existsById(numeroCartao);
    }

    public String getMotivoFalha() {
        return motivoFalha;
    }
}
