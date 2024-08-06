package com.br.vr;

import com.br.vr.controller.TransacaoController;
import com.br.vr.dto.TransacaoRequest;
import com.br.vr.service.CartaoService;
import com.br.vr.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
@ExtendWith(MockitoExtension.class)
public class TransacaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @Test
    void quandoRealizarTransacaoComSucesso_retornaStatus201() throws Exception {
        TransacaoRequest transacaoRequest = new TransacaoRequest();
        transacaoRequest.setNumeroCartao("6549873025634501");
        transacaoRequest.setSenhaCartao("1234");
        transacaoRequest.setValor(new BigDecimal("100.00"));

        when(transacaoService.realizarTransacao(transacaoRequest)).thenReturn(true);

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + transacaoRequest.getNumeroCartao() + "\",\"senhaCartao\":\"" + transacaoRequest.getSenhaCartao() + "\",\"valor\":" + transacaoRequest.getValor() + "}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }

    @Test
    void quandoRealizarTransacaoComSaldoInsuficiente_retornaStatus422() throws Exception {
        TransacaoRequest transacaoRequest = new TransacaoRequest();
        transacaoRequest.setNumeroCartao("6549873025634501");
        transacaoRequest.setSenhaCartao("1234");
        transacaoRequest.setValor(new BigDecimal("1000.00"));

        when(transacaoService.realizarTransacao(transacaoRequest)).thenReturn(false);
        when(transacaoService.getMotivoFalha()).thenReturn("SALDO_INSUFICIENTE");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + transacaoRequest.getNumeroCartao() + "\",\"senhaCartao\":\"" + transacaoRequest.getSenhaCartao() + "\",\"valor\":" + transacaoRequest.getValor() + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }

    @Test
    void quandoRealizarTransacaoComSenhaInvalida_retornaStatus422() throws Exception {
        TransacaoRequest transacaoRequest = new TransacaoRequest();
        transacaoRequest.setNumeroCartao("6549873025634501");
        transacaoRequest.setSenhaCartao("1234");
        transacaoRequest.setValor(new BigDecimal("100.00"));

        when(transacaoService.realizarTransacao(transacaoRequest)).thenReturn(false);
        when(transacaoService.getMotivoFalha()).thenReturn("SENHA_INVALIDA");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + transacaoRequest.getNumeroCartao() + "\",\"senhaCartao\":\"" + transacaoRequest.getSenhaCartao() + "\",\"valor\":" + transacaoRequest.getValor() + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    void quandoRealizarTransacaoComCartaoInexistente_retornaStatus422() throws Exception {
        TransacaoRequest transacaoRequest = new TransacaoRequest();
        transacaoRequest.setNumeroCartao("6549873025634501");
        transacaoRequest.setSenhaCartao("1234");
        transacaoRequest.setValor(new BigDecimal("100.00"));

        when(transacaoService.realizarTransacao(transacaoRequest)).thenReturn(false);
        when(transacaoService.getMotivoFalha()).thenReturn("CARTAO_INEXISTENTE");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + transacaoRequest.getNumeroCartao() + "\",\"senhaCartao\":\"" + transacaoRequest.getSenhaCartao() + "\",\"valor\":" + transacaoRequest.getValor() + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }
}