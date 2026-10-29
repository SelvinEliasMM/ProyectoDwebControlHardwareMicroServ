package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Repuestos;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RepuestosRepository extends JpaRepository<Repuestos, Integer> {

    @Query("SELECT r FROM Repuestos r WHERE r.estadoRegistro = true OR r.estadoRegistro IS NULL ORDER BY r.nombreRepuesto ASC")
    List<Repuestos> findByEstadoRegistroTrue();

    Optional<Repuestos> findByCodigoRepuesto(String codigoRepuesto);

    @Query("SELECT r FROM Repuestos r WHERE (r.estadoRegistro = true OR r.estadoRegistro IS NULL) AND r.stock <= r.stockMinimo")
    List<Repuestos> findStockBajo();
}
