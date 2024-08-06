package com.br.vr;

import com.br.vr.controller.TransacaoController;
import com.br.vr.service.CartaoService;
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
    private CartaoService cartaoService;

    @Test
    void quandoRealizarTransacaoComSucesso_retornaStatus201() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        BigDecimal valor = new BigDecimal("100.00");

        when(cartaoService.realizarTransacao(numeroCartao, senha, valor)).thenReturn(true);

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senhaCartao\":\"" + senha + "\",\"valor\":" + valor + "}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }

    @Test
    void quandoRealizarTransacaoComSaldoInsuficiente_retornaStatus422() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        BigDecimal valor = new BigDecimal("1000.00");

        when(cartaoService.realizarTransacao(numeroCartao, senha, valor)).thenReturn(false);
        when(cartaoService.getMotivoFalha()).thenReturn("SALDO_INSUFICIENTE");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senhaCartao\":\"" + senha + "\",\"valor\":" + valor + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }

    @Test
    void quandoRealizarTransacaoComSenhaInvalida_retornaStatus422() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        BigDecimal valor = new BigDecimal("100.00");

        when(cartaoService.realizarTransacao(numeroCartao, senha, valor)).thenReturn(false);
        when(cartaoService.getMotivoFalha()).thenReturn("SENHA_INVALIDA");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senhaCartao\":\"" + senha + "\",\"valor\":" + valor + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    void quandoRealizarTransacaoComCartaoInexistente_retornaStatus422() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        BigDecimal valor = new BigDecimal("100.00");

        when(cartaoService.realizarTransacao(numeroCartao, senha, valor)).thenReturn(false);
        when(cartaoService.getMotivoFalha()).thenReturn("CARTAO_INEXISTENTE");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senhaCartao\":\"" + senha + "\",\"valor\":" + valor + "}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }
}