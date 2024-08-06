package com.br.vr;

import com.br.vr.controller.CartaoController;
import com.br.vr.model.Cartao;
import com.br.vr.service.CartaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartaoController.class)
@ExtendWith(MockitoExtension.class)
public class CartaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;

    @Test
    void quandoCriarCartao_retornaStatus201() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        Cartao cartao = new Cartao(numeroCartao, senha);

        when(cartaoService.criarCartao(numeroCartao, senha)).thenReturn(cartao);
        when(cartaoService.existeCartao(numeroCartao)).thenReturn(false);

        mockMvc.perform(post("/cartoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"numeroCartao\":\"" + numeroCartao + "\",\"senha\":\"" + senha + "\"}"));
    }

    @Test
    void quandoCartaoJaExiste_retornaStatus422() throws Exception {
        String numeroCartao = "6549873025634501";
        String senha = "1234";
        Cartao cartao = new Cartao(numeroCartao, senha);

        when(cartaoService.existeCartao(numeroCartao)).thenReturn(true);

        mockMvc.perform(post("/cartoes")
                        .contentType("application/json")
                        .content("{\"numeroCartao\":\"" + numeroCartao + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\"numeroCartao\":\"" + numeroCartao + "\",\"senha\":\"" + senha + "\"}"));
    }

    @Test
    void quandoObterSaldoCartaoExistente_retornaStatus200() throws Exception {
        String numeroCartao = "6549873025634501";
        Cartao cartao = new Cartao(numeroCartao, "1234");

        when(cartaoService.obterSaldo(numeroCartao)).thenReturn(Optional.of(cartao));

        mockMvc.perform(get("/cartoes/{numeroCartao}", numeroCartao))
                .andExpect(status().isOk())
                .andExpect(content().string(cartao.getSaldo().toString()));
    }

    @Test
    void quandoObterSaldoCartaoInexistente_retornaStatus404() throws Exception {
        String numeroCartao = "6549873025634501";

        when(cartaoService.obterSaldo(numeroCartao)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cartoes/{numeroCartao}", numeroCartao))
                .andExpect(status().isNotFound());
    }
}
