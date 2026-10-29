
package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Usuarios; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByCorreo(String correo);
    Usuarios findFirstByCorreo(String correo);

    // ompatibilidad
    Usuarios findByCorreoAndPasswordHash(String correo, String passwordHash);
    Usuarios findFirstByCorreoAndPasswordHash(String correo, String passwordHash);
}

