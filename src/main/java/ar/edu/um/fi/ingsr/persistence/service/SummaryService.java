package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.Summary;
import ar.edu.um.fi.ingsr.persistence.dto.SummaryDTO;
import ar.edu.um.fi.ingsr.persistence.repository.DocumentHistoryRepository;
import ar.edu.um.fi.ingsr.persistence.repository.SummaryRepository;

@Service
public class SummaryService {

    private final SummaryRepository summaryRepository;             // cambio: inyección por constructor
    private final DocumentHistoryRepository documentHistoryRepository;

    public SummaryService(SummaryRepository summaryRepository,     // cambio: constructor
                          DocumentHistoryRepository documentHistoryRepository) {
        this.summaryRepository = summaryRepository;
        this.documentHistoryRepository = documentHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Summary> getAll() {                                // cambio: @Transactional readOnly agregado
        return summaryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Summary findById(Integer id) {                          // cambio: @Transactional readOnly agregado
        return summaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Summary not found: " + id)); // cambio: orElse(null) → orElseThrow
    }

    @Transactional
    public Summary save(Summary summary) {
        return summaryRepository.saveAndFlush(summary);
    }

    @Transactional
    public Summary create(SummaryDTO dto) {
        Summary summary = new Summary();
        summary.setDocumentHistory(documentHistoryRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found: " + dto.getDocumentId())));
        summary.setContent(dto.getContent());
        if (dto.getModelUsed() != null) summary.setModelUsed(dto.getModelUsed());
        return summaryRepository.saveAndFlush(summary);
    }

    @Transactional
    public Summary update(Integer id, SummaryDTO dto) {
        Summary summary = summaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Summary not found: " + id)); // cambio: orElse(null) + if → orElseThrow
        if (dto.getDocumentId() != null) summary.setDocumentHistory(documentHistoryRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found: " + dto.getDocumentId())));
        if (dto.getContent() != null) summary.setContent(dto.getContent());
        if (dto.getModelUsed() != null) summary.setModelUsed(dto.getModelUsed());
        return summaryRepository.saveAndFlush(summary);
    }

    @Transactional
    public void deleteById(Integer id) {                           // cambio: @Transactional agregado
        summaryRepository.deleteById(id);
    }
}