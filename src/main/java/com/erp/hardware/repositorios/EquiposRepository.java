
package com.erp.hardware.repositorios;
import com.erp.hardware.modelos.Equipos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer>{
    
    @Query("SELECT e FROM Equipos e WHERE e.estadoRegistro = true OR e.estadoRegistro IS NULL")
    List<Equipos> findByEstadoRegistroTrue(); // Filtro de activos

    java.util.Optional<Equipos> findByNumeroSerie(String numeroSerie);
}
