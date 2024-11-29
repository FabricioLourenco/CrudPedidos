package com.app.pedidos.dtos;

import com.app.pedidos.models.ClienteModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class PedidoDto {

    private UUID id;

    @NotBlank
    @Size(min = 2, max = 80)
    private String descricao;

    @NotNull(message = "O valor é obrigatorio")
    @Positive(message = "Valor tem que ser positivo")
    private Double valor;

    @NotBlank
    private String status;

    @NotNull(message = "O Id do cliente é obrigatorio")
    private UUID clienteId;
}
