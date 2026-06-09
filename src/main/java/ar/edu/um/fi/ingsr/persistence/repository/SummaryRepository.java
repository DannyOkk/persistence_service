package ar.edu.um.fi.ingsr.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.um.fi.ingsr.persistence.domain.Summary;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {

    /**
     * Buscar resumen por ID del documento.
     * Retorna el más reciente si hay múltiples versiones.
     */
    Optional<Summary> findTopByDocumentHistoryIdOrderByCreatedAtDesc(Integer documentId);

    /**
     * Buscar todos los resúmenes de un documento (historial de versiones).
     */
    List<Summary> findByDocumentHistoryIdOrderByCreatedAtDesc(Integer documentId);
}
