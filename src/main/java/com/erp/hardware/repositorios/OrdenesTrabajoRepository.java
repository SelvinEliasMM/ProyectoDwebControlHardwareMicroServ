
package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.OrdenesTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenesTrabajoRepository extends JpaRepository<OrdenesTrabajo, Integer> {
  
    @Query("SELECT o FROM OrdenesTrabajo o WHERE o.estadoRegistro = true OR o.estadoRegistro IS NULL")
    List<OrdenesTrabajo> findByEstadoRegistroTrue(); // Filtro de activos
}