package com.app.pedidos.controllers;

import com.app.pedidos.dtos.ClienteDto;
import com.app.pedidos.models.ClienteModel;
import com.app.pedidos.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<Object> salvarCliente(@RequestBody @Valid ClienteDto clienteDto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> mensagensDeErro = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(mensagensDeErro);
        }

        if (clienteService.cpfExiste(clienteDto.getCpf())) {
            return ResponseEntity.badRequest().body("CPF já cadastrado.");
        }

        var clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDto, clienteModel);
        return ResponseEntity.ok(clienteService.salvar(clienteModel));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteModel>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarClientePorId(@PathVariable UUID id) {
        Optional<ClienteModel> clienteOptional = clienteService.buscarPorId(id);
        if (clienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.ok(clienteOptional.get());
    }

    @PutMapping("/editar")
    public ResponseEntity<Object> editarCliente(@RequestBody @Valid ClienteDto clienteDto) {
        Optional<ClienteModel> clienteOptional = clienteService.buscarPorId(clienteDto.getId());
        if (clienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        var clienteExistente = clienteOptional.get();
        BeanUtils.copyProperties(clienteDto, clienteExistente, "id");
        return ResponseEntity.ok(clienteService.salvar(clienteExistente));
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Object> removerCliente(@PathVariable UUID id) {
        if (!clienteService.podeRemoverCliente(id)) {
            return ResponseEntity.badRequest().body("Cliente possui pedidos vinculados e não pode ser removido.");
        }

        clienteService.remover(id);
        return ResponseEntity.ok("Cliente removido com sucesso.");
    }
}
