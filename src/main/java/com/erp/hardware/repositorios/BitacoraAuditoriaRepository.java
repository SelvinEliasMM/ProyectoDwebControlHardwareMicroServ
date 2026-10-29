package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.BitacoraAuditoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BitacoraAuditoriaRepository extends JpaRepository<BitacoraAuditoria, Integer> {
    List<BitacoraAuditoria> findTop100ByOrderByFechaHoraDesc();
}
