package ar.edu.um.fi.ingsr.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Integer> {

    /**
     * Buscar documento por hash SHA-256 del contenido.
     * Usado para deduplicación: si ya existe un documento con el mismo contenido,
     * no se vuelve a procesar aunque tenga diferente nombre.
     */
    Optional<DocumentHistory> findByContentHash(String contentHash);

    /**
     * Buscar documentos por ID de usuario.
     */
    List<DocumentHistory> findByUserId(Long userId);
}
