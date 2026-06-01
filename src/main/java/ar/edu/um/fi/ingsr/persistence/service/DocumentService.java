package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.repository.DocumentHistoryRepository;
import ar.edu.um.fi.ingsr.persistence.repository.NotebookRepository;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotebookRepository notebookRepository;

    @Transactional(readOnly = false)
    public DocumentHistory save(DocumentHistory documentHistory) {
        return documentHistoryRepository.saveAndFlush(documentHistory);
    }

    @Transactional(readOnly = false)
    public DocumentHistory create(DocumentDTO dto) {
        DocumentHistory doc = new DocumentHistory();
        doc.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
        doc.setFilename(dto.getFilename());
        doc.setFilePath(dto.getFilePath());
        if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
        if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
        if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
        if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId()).orElse(null));
        return documentHistoryRepository.saveAndFlush(doc);
    }

    @Transactional(readOnly = false)
    public DocumentHistory update(Integer id, DocumentDTO dto) {
        DocumentHistory doc = documentHistoryRepository.findById(id).orElse(null);
        if (doc != null) {
            if (dto.getUserId() != null) doc.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found")));
            if (dto.getFilename() != null) doc.setFilename(dto.getFilename());
            if (dto.getFilePath() != null) doc.setFilePath(dto.getFilePath());
            if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
            if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
            if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
            if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId()).orElse(null));
            return documentHistoryRepository.saveAndFlush(doc);
        }
        return null;
    }
    
    public DocumentHistory findById(Integer id) {
        return documentHistoryRepository.findById(id).orElse(null);
    }
    
    public List<DocumentHistory> findAll() {
        return documentHistoryRepository.findAll();
    }
    
    @Transactional(readOnly = false)
    public void deleteById(Integer id) {
        documentHistoryRepository.deleteById(id);
    }
}
