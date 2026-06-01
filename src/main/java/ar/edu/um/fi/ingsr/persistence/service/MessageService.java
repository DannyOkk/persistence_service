package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.Message;
import ar.edu.um.fi.ingsr.persistence.dto.MessageDTO;
import ar.edu.um.fi.ingsr.persistence.repository.MessageRepository;
import ar.edu.um.fi.ingsr.persistence.repository.ConversationRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private ConversationRepository conversationRepository;

    @Transactional(readOnly = false)
    public Message create(MessageDTO dto) {
        Message message = new Message();
        message.setConversation(conversationRepository.findById(dto.getConversationId()).orElseThrow(() -> new RuntimeException("Conversation not found")));
        message.setRole(dto.getRole());
        message.setContent(dto.getContent());
        return messageRepository.saveAndFlush(message);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }
    
    public List<Message> findByConversationId(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }
}
