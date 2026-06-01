package ar.edu.um.fi.ingsr.persistence.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import ar.edu.um.fi.ingsr.persistence.domain.Message;
import ar.edu.um.fi.ingsr.persistence.dto.MessageDTO;
import ar.edu.um.fi.ingsr.persistence.service.MessageService;

@RestController
@RequestMapping("/api/v1/db/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> getAll(@RequestParam(required = false) Long conversationId) {
        if (conversationId != null) {
            return messageService.findByConversationId(conversationId);
        }
        return messageService.findAll();
    }

    @PostMapping
    public ResponseEntity<Message> create(@Valid @RequestBody MessageDTO dto) {
        return ResponseEntity.status(201).body(messageService.create(dto));
    }
}
