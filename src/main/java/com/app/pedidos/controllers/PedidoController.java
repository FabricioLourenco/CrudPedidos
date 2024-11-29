package com.app.pedidos.controllers;

import com.app.pedidos.dtos.PedidoDto;
import com.app.pedidos.models.ClienteModel;
import com.app.pedidos.models.PedidoModel;
import com.app.pedidos.repositories.ClienteRepository;
import com.app.pedidos.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    final private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/salvar")
    public ResponseEntity<Object> savePedido
            (@RequestBody @Valid PedidoDto pedidoDto, BindingResult result){

        if (result.hasErrors()){
            List<String> menssagensDeErro = result.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(menssagensDeErro);
        }

        if(pedidoDto.getClienteId() == null){
            return ResponseEntity
                    .badRequest().body("O pedido deve estar associado a um cliente");
        }

        Optional<ClienteModel> cliente =
                clienteRepository.findById(pedidoDto.getClienteId());
        if(!cliente.isPresent()){
            return  ResponseEntity.badRequest().
                    body("Cliente não encontro com Id fornecido");
        }

        PedidoModel pedidoModel = new PedidoModel();
        BeanUtils.copyProperties(pedidoDto, pedidoModel);

        pedidoModel.setClienteModel(cliente.get());
        return ResponseEntity.ok().body(
                pedidoService.save(pedidoModel));
    }

    @GetMapping("/lista")
    public ResponseEntity<List<PedidoModel>> getAllPedidos(){
        return ResponseEntity.ok().body(
                pedidoService.findAll());
    }

    @GetMapping("/listapedidoCliente/{id}")
    public ResponseEntity<List<PedidoModel>> getAllPedidosCliente( @PathVariable(value = "id") UUID id){
        return ResponseEntity.ok().body(
                pedidoService.findPedidosByClienteId(id));
    }

    @PostMapping("/editar")
    public ResponseEntity<Object> editarPedido(
            @RequestBody @Valid PedidoDto pedidoDto)
    {
        var pedidoModel = new PedidoModel();
        BeanUtils.copyProperties(pedidoDto, pedidoModel);

        Optional<PedidoModel> pedidoModelOptional =
                pedidoService.findById(pedidoModel.getId());

        if(!pedidoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Pedido não encontrado"
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                pedidoService.save(pedidoModel)
        );
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> apagarPedido(
            @PathVariable(value = "id") UUID id
    ){
        Optional<PedidoModel> pedidoModelOptional =
                pedidoService.findById(id);

        if(!pedidoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "Pedido não encontrado"
            );
        }

        pedidoService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                "Pedido apagado com sucesso"
        );
    }
}
