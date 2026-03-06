package com.treinamento.produto.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.treinamento.model.Produto;
import com.treinamento.repository.ProdutoRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deveConectarNoBancoDeTeste() {
        Integer resultado = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertNotNull(resultado);
        assertEquals(1, resultado);
    }

    @Test
    void deveCarregarSeedCom10Produtos() {
        long total = produtoRepository.count();
        assertEquals(10, total);
    }

    @Test
    void deveBuscarProdutosPorNome() {
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase("notebook");
        assertFalse(produtos.isEmpty());
        assertEquals("Notebook Dell Inspiron 15", produtos.get(0).getNome());
    }
}
