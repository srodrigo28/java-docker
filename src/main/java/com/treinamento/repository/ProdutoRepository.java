package com.treinamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.treinamento.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
