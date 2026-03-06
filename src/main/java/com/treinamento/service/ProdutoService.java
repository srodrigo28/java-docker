package com.treinamento.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.treinamento.dto.produto.ProdutoRequestDTO;
import com.treinamento.dto.produto.ProdutoResponseDTO;
import com.treinamento.exception.ResourceNotFoundException;
import com.treinamento.mapper.ProdutoMapper;
import com.treinamento.model.Produto;
import com.treinamento.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validar(dto);
        Produto salvo = produtoRepository.save(ProdutoMapper.toEntity(dto));
        return ProdutoMapper.toResponse(salvo);
    }

    public List<ProdutoResponseDTO> listar(String nome) {
        List<Produto> produtos = (nome == null || nome.isBlank())
                ? produtoRepository.findAll()
                : produtoRepository.findByNomeContainingIgnoreCase(nome);
        return produtos.stream().map(ProdutoMapper::toResponse).toList();
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = buscarEntidadePorId(id);
        return ProdutoMapper.toResponse(produto);
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        validar(dto);
        Produto produto = buscarEntidadePorId(id);
        ProdutoMapper.updateEntity(produto, dto);
        Produto atualizado = produtoRepository.save(produto);
        return ProdutoMapper.toResponse(atualizado);
    }

    public void deletar(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado com id: " + id));
    }

    private void validar(ProdutoRequestDTO dto) {
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto e obrigatorio.");
        }
        if (dto.descricao() == null || dto.descricao().isBlank()) {
            throw new IllegalArgumentException("Descricao do produto e obrigatoria.");
        }
        if (dto.qtd() == null || dto.qtd() < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
        }
        if (dto.valor() == null || dto.valor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (dto.dataCompra() == null) {
            throw new IllegalArgumentException("Data de compra e obrigatoria.");
        }
    }
}
