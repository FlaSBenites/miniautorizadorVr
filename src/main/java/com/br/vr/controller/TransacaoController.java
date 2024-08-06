package com.br.vr.controller;


import com.br.vr.dto.TransacaoRequest;
import com.br.vr.service.CartaoService;
import com.br.vr.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoRequest transacaoRequest) {
        boolean transacaoAutorizada = transacaoService.realizarTransacao(transacaoRequest);

        if (transacaoAutorizada) {
            return ResponseEntity.status(HttpStatus.CREATED).body("OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(transacaoService.getMotivoFalha());
        }
    }
}
