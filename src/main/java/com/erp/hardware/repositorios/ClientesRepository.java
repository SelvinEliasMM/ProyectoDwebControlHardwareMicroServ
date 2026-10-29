package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Clientes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Integer> {
    // genera el query automáticamente por el nombre del método
    @Query("SELECT c FROM Clientes c WHERE c.estadoRegistro = true OR c.estadoRegistro IS NULL")
    List<Clientes> findByEstadoRegistroTrue();

    java.util.Optional<Clientes> findByEmail(String email);
}
