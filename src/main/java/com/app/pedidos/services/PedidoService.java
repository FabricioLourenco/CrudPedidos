package com.app.pedidos.services;

import com.app.pedidos.models.PedidoModel;
import com.app.pedidos.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public PedidoModel save(PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    public List<PedidoModel> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoModel> findById(UUID id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public void delete(UUID id) {
        pedidoRepository.deleteById(id);
    }

    public List<PedidoModel> findPedidosByClienteId(UUID idCliente) {
        return pedidoRepository.findByClienteModel_Id(idCliente);
    }
}
