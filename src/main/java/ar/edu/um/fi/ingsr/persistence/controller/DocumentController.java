package ar.edu.um.fi.ingsr.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.service.DocumentService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/db/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

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
}
