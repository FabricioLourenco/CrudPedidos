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
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("cliente")
public class ClienteController {

    final private ClienteService clienteService;

    public ClienteController(ClienteService clienteService, View error) {
        this.clienteService = clienteService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<Object> savecliete(@RequestBody @Valid ClienteDto clienteDto, BindingResult result){

        if (result.hasErrors()){
            List<String> menssagensDeErro = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(menssagensDeErro);
        }
        var clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDto, clienteModel);
        return ResponseEntity.ok().body(clienteService.save(clienteModel));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteModel>> getAllCliente(){
        return ResponseEntity.ok().body(
                clienteService.findAll());
    }

    @PostMapping("/editar")
    public ResponseEntity<Object> editarCliente(
            @RequestBody @Valid ClienteModel clienteModel) {

        Optional<ClienteModel> clienteModelOptional = clienteService.findById(clienteModel.getId());

        if (clienteModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        ClienteModel clienteExistente = clienteModelOptional.get();

        BeanUtils.copyProperties(clienteModel, clienteExistente, "id", "pedidos");

        ClienteModel clienteAtualizado = clienteService.save(clienteExistente);

        return ResponseEntity.ok(clienteAtualizado);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> apagarCliente(
            @PathVariable(value = "id") UUID id
    ){
        Optional<ClienteModel> clienteModelOptional =
                clienteService.findById(id);
        if(clienteModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Cliente não encontrado"
            );
        }

        clienteService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                "Cliente apagado com sucesso"
        );
    }

}
