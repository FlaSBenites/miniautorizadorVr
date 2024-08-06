package com.br.vr.controller;


import com.br.vr.model.Cartao;
import com.br.vr.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<Cartao> criarCartao(@RequestBody Cartao cartaoRequest) {
        if (cartaoService.existeCartao(cartaoRequest.getNumeroCartao())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoRequest);
        }

        Cartao novoCartao = cartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCartao);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        Optional<Cartao> cartao = cartaoService.obterSaldo(numeroCartao);

        if (cartao.isPresent()) {
            return ResponseEntity.ok(cartao.get().getSaldo());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
