package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.DetalleRepuestosOrden;
import com.erp.hardware.modelos.OrdenesTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleRepuestosOrdenRepository extends JpaRepository<DetalleRepuestosOrden, Integer> {
    List<DetalleRepuestosOrden> findByIdOrdenOrderByFechaRegistroAsc(OrdenesTrabajo orden);
}
