package com.app.pedidos.repositories;

import com.app.pedidos.models.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, UUID> {

    List<PedidoModel> findByClienteModel_Id(UUID idCliente);

    Optional<PedidoModel> findByIdAndClienteModel_Id(UUID idPedido, UUID idCliente);

    void deleteByClienteModel_Id(UUID idCliente);

    boolean existsByClienteModel_IdAndStatus(UUID idCliente, String status);
}
