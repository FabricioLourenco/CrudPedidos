package com.app.pedidos.services;

import com.app.pedidos.dtos.PedidoDto;
import com.app.pedidos.models.ClienteModel;
import com.app.pedidos.models.PedidoModel;
import com.app.pedidos.repositories.ClienteRepository;
import com.app.pedidos.repositories.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Optional<PedidoModel> salvarPedidoVinculado(PedidoDto pedidoDto) {
        Optional<ClienteModel> clienteOptional = clienteRepository.findById(pedidoDto.getClienteId());
        if (clienteOptional.isEmpty()) {
            return Optional.empty();
        }

        PedidoModel pedidoModel = new PedidoModel();
        pedidoModel.setClienteModel(clienteOptional.get());
        pedidoModel.setDescricao(pedidoDto.getDescricao());
        pedidoModel.setValor(pedidoDto.getValor());
        pedidoModel.setStatus(pedidoDto.getStatus());
        return Optional.of(pedidoRepository.save(pedidoModel));
    }

    public List<PedidoModel> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoModel> buscarPorId(UUID id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Optional<PedidoModel> atualizarPedido(PedidoDto pedidoDto) {
        Optional<PedidoModel> pedidoOptional = buscarPorId(pedidoDto.getId());
        if (pedidoOptional.isEmpty()) {
            return Optional.empty();
        }

        PedidoModel pedidoExistente = pedidoOptional.get();
        pedidoExistente.setDescricao(pedidoDto.getDescricao());
        pedidoExistente.setValor(pedidoDto.getValor());
        pedidoExistente.setStatus(pedidoDto.getStatus());
        return Optional.of(pedidoRepository.save(pedidoExistente));
    }

    public boolean podeRemoverPedido(UUID id) {
        return buscarPorId(id).map(pedido -> !"Enviado".equalsIgnoreCase(pedido.getStatus())).orElse(false);
    }

    @Transactional
    public void remover(UUID id) {
        pedidoRepository.deleteById(id);
    }
}
