package com.treinamento.produto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.treinamento.dto.produto.ProdutoRequestDTO;
import com.treinamento.dto.produto.ProdutoResponseDTO;
import com.treinamento.exception.ResourceNotFoundException;
import com.treinamento.model.Produto;
import com.treinamento.repository.ProdutoRepository;
import com.treinamento.service.ProdutoService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProdutoServiceTest {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Mousepad XL",
                "Mousepad grande de tecido",
                14,
                new BigDecimal("89.90"),
                LocalDate.of(2026, 2, 10)
        );

        ProdutoResponseDTO response = produtoService.criar(request);

        assertNotNull(response.id());
        assertEquals("Mousepad XL", response.nome());
        assertEquals(new BigDecimal("89.90"), response.valor());
    }

    @Test
    void naoDeveCriarProdutoComValorInvalido() {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Produto Invalido",
                "Valor zero deve falhar",
                1,
                BigDecimal.ZERO,
                LocalDate.of(2026, 2, 10)
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> produtoService.criar(request));
        assertEquals("Valor deve ser maior que zero.", ex.getMessage());
    }

    @Test
    void deveAtualizarProdutoExistente() {
        Produto base = produtoRepository.findByNomeContainingIgnoreCase("Notebook").get(0);

        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Notebook Dell Inspiron 15 Atualizado",
                "Notebook atualizado com 32GB RAM",
                5,
                new BigDecimal("4299.90"),
                LocalDate.of(2026, 2, 12)
        );

        ProdutoResponseDTO response = produtoService.atualizar(base.getId(), request);

        assertEquals(base.getId(), response.id());
        assertEquals("Notebook Dell Inspiron 15 Atualizado", response.nome());
        assertEquals(5, response.qtd());
        assertEquals(new BigDecimal("4299.90"), response.valor());
    }

    @Test
    void naoDeveAtualizarProdutoInexistente() {
        ProdutoRequestDTO request = new ProdutoRequestDTO(
                "Nao Existe",
                "Produto inexistente",
                1,
                new BigDecimal("10.00"),
                LocalDate.of(2026, 2, 10)
        );

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> produtoService.atualizar(999999L, request)
        );
        assertTrue(ex.getMessage().contains("Produto nao encontrado"));
    }
}
