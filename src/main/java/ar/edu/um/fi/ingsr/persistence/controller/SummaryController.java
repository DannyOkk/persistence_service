package ar.edu.um.fi.ingsr.persistence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ar.edu.um.fi.ingsr.persistence.domain.Summary;
import ar.edu.um.fi.ingsr.persistence.dto.SummaryDTO;
import ar.edu.um.fi.ingsr.persistence.service.SummaryService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/db/summaries")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public List<Summary> findAll() {
        return summaryService.getAll();
    }

    @PostMapping
    public Summary save(@Valid @RequestBody SummaryDTO summary) {
        return summaryService.create(summary);
    }

    @GetMapping("/{id}")
    public Summary findById(@PathVariable("id") Integer id) {
        return summaryService.findById(id);
    }

    @PatchMapping("/{id}")
    public Summary update(@PathVariable("id") Integer id, @RequestBody SummaryDTO summary) {
        return summaryService.update(id, summary);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        summaryService.deleteById(id);
    }
}
