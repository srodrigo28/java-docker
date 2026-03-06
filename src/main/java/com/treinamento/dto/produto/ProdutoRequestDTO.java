package com.treinamento.dto.produto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoRequestDTO(
        String nome,
        String descricao,
        Integer qtd,
        BigDecimal valor,
        LocalDate dataCompra
) {
}
