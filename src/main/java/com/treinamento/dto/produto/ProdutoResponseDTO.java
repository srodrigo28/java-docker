package com.treinamento.dto.produto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Integer qtd,
        BigDecimal valor,
        LocalDate dataCompra
) {
}
