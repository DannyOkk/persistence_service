package ar.edu.um.fi.ingsr.persistence.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import ar.edu.um.fi.ingsr.persistence.domain.Notebook;
import ar.edu.um.fi.ingsr.persistence.dto.NotebookDTO;
import ar.edu.um.fi.ingsr.persistence.service.NotebookService;

@RestController
@RequestMapping("/api/v1/db/notebooks")
public class NotebookController {
    
    @Autowired
    private NotebookService notebookService;

    @GetMapping
    public List<Notebook> getAll(@RequestParam(required = false) Long userId) {
        if (userId != null) {
            return notebookService.findByUserId(userId);
        }
        return notebookService.findAll();
    }

    @PostMapping
    public ResponseEntity<Notebook> create(@Valid @RequestBody NotebookDTO dto) {
        return ResponseEntity.status(201).body(notebookService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notebook> getById(@PathVariable("id") Long id) {
        Notebook notebook = notebookService.findById(id);
        if (notebook == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(notebook);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Notebook> update(@PathVariable("id") Long id, @RequestBody NotebookDTO dto) {
        Notebook notebook = notebookService.update(id, dto);
        if (notebook == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(notebook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (notebookService.findById(id) == null) return ResponseEntity.notFound().build();
        notebookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
