package com.app.pedidos.dtos;
import com.app.pedidos.models.PedidoModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
public class ClienteDto {

    private UUID id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    private String endereco;

    private List<PedidoModel> pedidos = new ArrayList<>();
}
