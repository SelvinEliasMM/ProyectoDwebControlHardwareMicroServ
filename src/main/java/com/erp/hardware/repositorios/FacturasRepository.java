package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Facturas;
import com.erp.hardware.modelos.OrdenesTrabajo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasRepository extends JpaRepository<Facturas, Integer> {

    @Query("SELECT f FROM Facturas f WHERE f.estadoRegistro = true OR f.estadoRegistro IS NULL ORDER BY f.fechaEmision DESC")
    List<Facturas> findByEstadoRegistroTrue();

    Optional<Facturas> findByIdOrden(OrdenesTrabajo orden);

    Optional<Facturas> findByNumeroFactura(String numeroFactura);
}
