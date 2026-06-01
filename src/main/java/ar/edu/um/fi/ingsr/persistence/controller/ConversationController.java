package ar.edu.um.fi.ingsr.persistence.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import ar.edu.um.fi.ingsr.persistence.domain.Conversation;
import ar.edu.um.fi.ingsr.persistence.dto.ConversationDTO;
import ar.edu.um.fi.ingsr.persistence.service.ConversationService;

@RestController
@RequestMapping("/api/v1/db/conversations")
public class ConversationController {
    
    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public List<Conversation> getAll(@RequestParam(required = false) Long notebookId) {
        if (notebookId != null) {
            return conversationService.findByNotebookId(notebookId);
        }
        return conversationService.findAll();
    }

    @PostMapping
    public ResponseEntity<Conversation> create(@Valid @RequestBody ConversationDTO dto) {
        return ResponseEntity.status(201).body(conversationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getById(@PathVariable("id") Long id) {
        Conversation conversation = conversationService.findById(id);
        if (conversation == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(conversation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (conversationService.findById(id) == null) return ResponseEntity.notFound().build();
        conversationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
