package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.HistorialEstados;
import com.erp.hardware.modelos.OrdenesTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialEstadosRepository extends JpaRepository<HistorialEstados, Integer> {
    List<HistorialEstados> findByIdOrdenOrderByFechaCambioDesc(OrdenesTrabajo orden);
}
