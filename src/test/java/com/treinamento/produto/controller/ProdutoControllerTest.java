package com.treinamento.produto.controller;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.treinamento.model.Produto;
import com.treinamento.repository.ProdutoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProdutoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deveListarTodosProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(10)));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        Produto produto = produtoRepository.findByNomeContainingIgnoreCase("Notebook").get(0);

        mockMvc.perform(get("/produtos/{id}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produto.getId()))
                .andExpect(jsonPath("$.nome").value(produto.getNome()));
    }

    @Test
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        mockMvc.perform(get("/produtos/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Produto nao encontrado com id: 999999"));
    }
}
