package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.Notebook;
import ar.edu.um.fi.ingsr.persistence.dto.NotebookDTO;
import ar.edu.um.fi.ingsr.persistence.repository.NotebookRepository;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;

@Service
public class NotebookService {
    @Autowired
    private NotebookRepository notebookRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = false)
    public Notebook create(NotebookDTO dto) {
        Notebook notebook = new Notebook();
        notebook.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        notebook.setName(dto.getName());
        if (dto.getDescription() != null) notebook.setDescription(dto.getDescription());
        return notebookRepository.saveAndFlush(notebook);
    }

    @Transactional(readOnly = false)
    public Notebook update(Long id, NotebookDTO dto) {
        Notebook notebook = notebookRepository.findById(id).orElse(null);
        if (notebook != null) {
            if (dto.getUserId() != null) notebook.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            if (dto.getName() != null) notebook.setName(dto.getName());
            if (dto.getDescription() != null) notebook.setDescription(dto.getDescription());
            return notebookRepository.saveAndFlush(notebook);
        }
        return null;
    }

    public Notebook findById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    public List<Notebook> findAll() {
        return notebookRepository.findAll();
    }
    
    public List<Notebook> findByUserId(Long userId) {
        return notebookRepository.findByUserId(userId);
    }

    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        notebookRepository.deleteById(id);
    }
}
