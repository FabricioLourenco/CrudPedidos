package com.app.pedidos.controllers;

import com.app.pedidos.dtos.PedidoDto;
import com.app.pedidos.models.PedidoModel;
import com.app.pedidos.services.PedidoService;
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
@RequestMapping("pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<Object> salvarPedido(@RequestBody @Valid PedidoDto pedidoDto, BindingResult result) {
        if (result.hasErrors()) {
            List<String> mensagensDeErro = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(mensagensDeErro);
        }

        Optional<PedidoModel> pedidoCriado = pedidoService.salvarPedidoVinculado(pedidoDto);
        if (pedidoCriado.isEmpty()) {
            return ResponseEntity.badRequest().body("Cliente não encontrado ou dados inválidos.");
        }

        return ResponseEntity.ok(pedidoCriado.get());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PedidoModel>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPedidoPorId(@PathVariable UUID id) {
        Optional<PedidoModel> pedidoOptional = pedidoService.buscarPorId(id);
        if (pedidoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
        return ResponseEntity.ok(pedidoOptional.get());
    }

    @PutMapping("/editar")
    public ResponseEntity<Object> editarPedido(@RequestBody @Valid PedidoDto pedidoDto) {
        Optional<PedidoModel> pedidoAtualizado = pedidoService.atualizarPedido(pedidoDto);
        if (pedidoAtualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }

        return ResponseEntity.ok(pedidoAtualizado.get());
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Object> removerPedido(@PathVariable UUID id) {
        if (!pedidoService.podeRemoverPedido(id)) {
            return ResponseEntity.badRequest().body("Pedido não pode ser removido devido ao status atual.");
        }

        pedidoService.remover(id);
        return ResponseEntity.ok("Pedido removido com sucesso.");
    }
}
