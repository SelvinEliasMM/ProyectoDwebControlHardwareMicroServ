package com.erp.hardware.repositorios;

import com.erp.hardware.modelos.Diagnosticos;
import com.erp.hardware.modelos.OrdenesTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticosRepository extends JpaRepository<Diagnosticos, Integer> {
    List<Diagnosticos> findByIdOrdenOrderByFechaDiagnosticoDesc(OrdenesTrabajo orden);
}
