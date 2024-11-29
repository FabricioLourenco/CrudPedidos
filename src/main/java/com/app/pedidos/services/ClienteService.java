package com.app.pedidos.services;

import com.app.pedidos.models.ClienteModel;
import com.app.pedidos.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel salvar(ClienteModel clienteModel) {
        return clienteRepository.save(clienteModel);
    }

    public List<ClienteModel> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<ClienteModel> buscarPorId(UUID id) {
        return clienteRepository.findById(id);
    }

    public boolean cpfExiste(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public boolean podeRemoverCliente(UUID id) {
        return buscarPorId(id).map(cliente -> cliente.getPedidos().isEmpty()).orElse(false);
    }

    @Transactional
    public void remover(UUID id) {
        clienteRepository.deleteById(id);
    }
}
