package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.Conversation;
import ar.edu.um.fi.ingsr.persistence.dto.ConversationDTO;
import ar.edu.um.fi.ingsr.persistence.repository.ConversationRepository;
import ar.edu.um.fi.ingsr.persistence.repository.NotebookRepository;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    
    @Autowired
    private NotebookRepository notebookRepository;

    @Transactional(readOnly = false)
    public Conversation create(ConversationDTO dto) {
        Conversation conversation = new Conversation();
        conversation.setNotebook(notebookRepository.findById(dto.getNotebookId()).orElseThrow(() -> new RuntimeException("Notebook not found")));
        conversation.setTitle(dto.getTitle());
        return conversationRepository.saveAndFlush(conversation);
    }

    public Conversation findById(Long id) {
        return conversationRepository.findById(id).orElse(null);
    }

    public List<Conversation> findAll() {
        return conversationRepository.findAll();
    }
    
    public List<Conversation> findByNotebookId(Long notebookId) {
        return conversationRepository.findByNotebookId(notebookId);
    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        conversationRepository.deleteById(id);
    }
}
