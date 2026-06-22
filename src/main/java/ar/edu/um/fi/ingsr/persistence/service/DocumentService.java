package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.repository.DocumentHistoryRepository;
import ar.edu.um.fi.ingsr.persistence.repository.NotebookRepository;
import ar.edu.um.fi.ingsr.persistence.repository.UserRepository;

@Service
public class DocumentService {

    private final DocumentHistoryRepository documentHistoryRepository;
    private final UserRepository userRepository;
    private final NotebookRepository notebookRepository;

    public DocumentService(DocumentHistoryRepository documentHistoryRepository,
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
        if (dto.getContentHash() != null) {
            Optional<DocumentHistory> existing = documentHistoryRepository.findByContentHash(dto.getContentHash());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        DocumentHistory doc = new DocumentHistory();
        doc.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId())));
        doc.setFilename(dto.getFilename());
        doc.setFilePath(dto.getFilePath());
        if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
        if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
        if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
        if (dto.getContentHash() != null) doc.setContentHash(dto.getContentHash());
        if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId())
                .orElseThrow(() -> new RuntimeException("Notebook not found: " + dto.getNotebookId())));
        return documentHistoryRepository.saveAndFlush(doc);
    }

    @Transactional
    public DocumentHistory update(Integer id, DocumentDTO dto) {
        DocumentHistory doc = documentHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));
        if (dto.getUserId() != null) doc.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId())));
        if (dto.getFilename() != null) doc.setFilename(dto.getFilename());
        if (dto.getFilePath() != null) doc.setFilePath(dto.getFilePath());
        if (dto.getJobId() != null) doc.setJobId(dto.getJobId());
        if (dto.getStatus() != null) doc.setStatus(dto.getStatus());
        if (dto.getExtractedText() != null) doc.setExtractedText(dto.getExtractedText());
        if (dto.getContentHash() != null) doc.setContentHash(dto.getContentHash());
        if (dto.getNotebookId() != null) doc.setNotebook(notebookRepository.findById(dto.getNotebookId())
                .orElseThrow(() -> new RuntimeException("Notebook not found: " + dto.getNotebookId())));
        return documentHistoryRepository.saveAndFlush(doc);
    }

    @Transactional(readOnly = true)
    public DocumentHistory findById(Integer id) {
        return documentHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<DocumentHistory> findAll() {
        return documentHistoryRepository.findAll();
    }

    /**
     * Buscar documento por hash SHA-256 del contenido.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentHistory> findByContentHash(String contentHash) {
        return documentHistoryRepository.findByContentHash(contentHash);
    }

    /**
     * Buscar documentos por ID de usuario.
     */
    @Transactional(readOnly = true)
    public List<DocumentHistory> findByUserId(Long userId) {
        return documentHistoryRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteById(Integer id) {
        documentHistoryRepository.deleteById(id);
    }
}