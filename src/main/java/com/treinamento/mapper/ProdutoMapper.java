package com.treinamento.mapper;

import com.treinamento.dto.produto.ProdutoRequestDTO;
import com.treinamento.dto.produto.ProdutoResponseDTO;
import com.treinamento.model.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setQtd(dto.qtd());
        produto.setValor(dto.valor());
        produto.setDataCompra(dto.dataCompra());
        return produto;
    }

    public static void updateEntity(Produto produto, ProdutoRequestDTO dto) {
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setQtd(dto.qtd());
        produto.setValor(dto.valor());
        produto.setDataCompra(dto.dataCompra());
    }

    public static ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getQtd(),
                produto.getValor(),
                produto.getDataCompra()
        );
    }
}
