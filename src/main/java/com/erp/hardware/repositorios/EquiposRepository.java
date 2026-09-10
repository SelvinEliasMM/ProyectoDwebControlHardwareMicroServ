
package com.erp.hardware.repositorios;
import com.erp.hardware.modelos.Equipos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer>{
    
}
