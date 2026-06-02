package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.repository.DocumentHistoryRepository;
import ar.edu.um.fi.ingsr.persistence.repository.NotebookRepository;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;

@Service
public class DocumentService {

    private final DocumentHistoryRepository documentHistoryRepository;  // cambio: inyección por constructor
    private final UserRepository userRepository;
    private final NotebookRepository notebookRepository;

    public DocumentService(DocumentHistoryRepository documentHistoryRepository,  // cambio: constructor
                           UserRepository userRepository,
                           NotebookRepository notebookRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.userRepository = userRepository;
        this.notebookRepository = notebookRepository;
    }

    @Transactional
    public DocumentHistory save(DocumentHistory documentHistory) {
        return documentHistoryRepository.saveAndFlush(documentHistory);
    }

    @Transactional
    public DocumentHistory create(DocumentDTO dto) {
        DocumentHistory doc = new DocumentHistory();
        doc.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId())));
        doc.setFilename(dto.getFilename());
        doc.setFilePath(dto.getFilePath());
        if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
        if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
        if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
        if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId())
                .orElseThrow(() -> new RuntimeException("Notebook not found: " + dto.getNotebookId()))); // cambio: orElse(null) → orElseThrow
        return documentHistoryRepository.saveAndFlush(doc);
    }

    @Transactional
    public DocumentHistory update(Integer id, DocumentDTO dto) {
        DocumentHistory doc = documentHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id)); // cambio: orElse(null) + if → orElseThrow
        if (dto.getUserId() != null) doc.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId())));
        if (dto.getFilename() != null) doc.setFilename(dto.getFilename());
        if (dto.getFilePath() != null) doc.setFilePath(dto.getFilePath());
        if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
        if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
        if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
        if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId())
                .orElseThrow(() -> new RuntimeException("Notebook not found: " + dto.getNotebookId()))); // cambio: orElse(null) → orElseThrow
        return documentHistoryRepository.saveAndFlush(doc);
    }

    @Transactional(readOnly = true)
    public DocumentHistory findById(Integer id) {
        return documentHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id)); // cambio: orElse(null) → orElseThrow
    }

    @Transactional(readOnly = true)
    public List<DocumentHistory> findAll() {
        return documentHistoryRepository.findAll();
    }

    @Transactional
    public void deleteById(Integer id) {
        documentHistoryRepository.deleteById(id);
    }
}