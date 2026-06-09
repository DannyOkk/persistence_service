package ar.edu.um.fi.ingsr.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.domain.Summary;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.service.DocumentService;
import ar.edu.um.fi.ingsr.persistence.service.SummaryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/db/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public List<DocumentHistory> findAll() {
        return documentService.findAll();
    }

    @PostMapping
    public DocumentHistory save(@Valid @RequestBody DocumentDTO document) {
        return documentService.create(document);
    }

    @GetMapping("/{id}")
    public DocumentHistory findById(@PathVariable("id") Integer id) {
        return documentService.findById(id);
    }

    @PatchMapping("/{id}")
    public DocumentHistory update(@PathVariable("id") Integer id, @RequestBody DocumentDTO document) {
        return documentService.update(id, document);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        documentService.deleteById(id);
    }

    /**
     * Buscar documento por hash SHA-256 del contenido.
     * Se usa para deduplicación: si ya existe un documento con el mismo
     * contenido (independientemente del nombre del archivo), se devuelve
     * el documento existente sin volver a procesar.
     *
     * @param hash SHA-256 hash del contenido del archivo
     * @return Documento si existe, 404 si no
     */
    @GetMapping("/hash/{hash}")
    public ResponseEntity<DocumentHistory> findByHash(@PathVariable("hash") String hash) {
        return documentService.findByContentHash(hash)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener el resumen más reciente de un documento.
     *
     * @param id Document ID
     * @return Resumen o 404 si no existe
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getDocumentSummary(@PathVariable("id") Integer id) {
        return summaryService.findByDocumentId(id)
                .map(summary -> ResponseEntity.ok(Map.of(
                        "id", summary.getId(),
                        "content", summary.getContent(),
                        "modelUsed", summary.getModelUsed(),
                        "createdAt", summary.getCreatedAt().toString()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener documentos por ID de usuario.
     */
    @GetMapping("/user/{userId}")
    public List<DocumentHistory> findByUserId(@PathVariable("userId") Long userId) {
        return documentService.findByUserId(userId);
    }
}
