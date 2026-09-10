
package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Usuarios; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {
    // Este método busca automáticamente en la base de datos por estas dos columnas
    Usuarios findByCorreoAndPasswordHash(String correo, String passwordHash);
}

