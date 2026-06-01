package ar.edu.um.fi.ingsr.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.um.fi.ingsr.persistence.domain.Summary;
import ar.edu.um.fi.ingsr.persistence.dto.SummaryDTO;
import ar.edu.um.fi.ingsr.persistence.repository.SummaryRepository;
import ar.edu.um.fi.ingsr.persistence.repository.DocumentHistoryRepository;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;
    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    public List<Summary> getAll() {
        return summaryRepository.findAll();
    }
    
    public Summary findById(Integer id) {
        return summaryRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public Summary save(Summary summary) {
        return summaryRepository.saveAndFlush(summary);
    }

    @Transactional(readOnly = false)
    public Summary create(SummaryDTO dto) {
        Summary summary = new Summary();
        summary.setDocumentHistory(documentHistoryRepository.findById(dto.getDocumentId()).orElseThrow(() -> new RuntimeException("Document not found")));
        summary.setContent(dto.getContent());
        if (dto.getModelUsed() != null) summary.setModelUsed(dto.getModelUsed());
        return summaryRepository.saveAndFlush(summary);
    }

    @Transactional(readOnly = false)
    public Summary update(Integer id, SummaryDTO dto) {
        Summary summary = summaryRepository.findById(id).orElse(null);
        if (summary != null) {
            if (dto.getDocumentId() != null) summary.setDocumentHistory(documentHistoryRepository.findById(dto.getDocumentId()).orElseThrow(() -> new RuntimeException("Document not found")));
            if (dto.getContent() != null) summary.setContent(dto.getContent());
            if (dto.getModelUsed() != null) summary.setModelUsed(dto.getModelUsed());
            return summaryRepository.saveAndFlush(summary);
        }
        return null;
    }

    public void deleteById(Integer id) {
        summaryRepository.deleteById(id);
    }
}
