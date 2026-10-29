package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.MovimientosInventario;
import com.erp.hardware.modelos.Repuestos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientosInventarioRepository extends JpaRepository<MovimientosInventario, Integer> {
    List<MovimientosInventario> findByIdRepuestoOrderByFechaMovimientoDesc(Repuestos repuesto);
    List<MovimientosInventario> findTop50ByOrderByFechaMovimientoDesc();
}
