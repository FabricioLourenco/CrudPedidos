package com.app.pedidos.services;

import com.app.pedidos.models.ClienteModel;
import com.app.pedidos.models.PedidoModel;
import com.app.pedidos.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel save(ClienteModel clienteModel) {

        for (PedidoModel pedido: clienteModel.getPedidos()) {

            pedido.setClienteModel(clienteModel);
        }

        return clienteRepository.save(clienteModel);
    }

    @Transactional
    public void delete(UUID id) {
        clienteRepository.deleteById(id);
    }

    public List<ClienteModel> findAll() {
        return clienteRepository.findAll();
    }
    public Optional<ClienteModel> findById(UUID id) {
        return clienteRepository.findById(id);
    }


}
