package ar.edu.um.fi.ingsr.persistence.controller;

import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.domain.Summary;
import ar.edu.um.fi.ingsr.persistence.dto.SummaryDTO;
import ar.edu.um.fi.ingsr.persistence.service.SummaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;  // cambio: import nuevo
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SummaryController.class)
public class SummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean                                                             // cambio: @MockBean → @MockitoBean
    private SummaryService summaryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {
        List<Summary> list = new ArrayList<>();
        Summary summary = new Summary();
        summary.setId(500);
        summary.setContent("This is a summary content.");
        summary.setModelUsed("gpt-4o");
        list.add(summary);

        when(summaryService.getAll()).thenReturn(list);

        mockMvc.perform(get("/api/v1/db/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(500))
                .andExpect(jsonPath("$[0].content").value("This is a summary content."))
                .andExpect(jsonPath("$[0].modelUsed").value("gpt-4o"));
    }

    @Test
    public void testCreateSummary() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("documentId", 123);
        payload.put("content", "New summary content.");
        payload.put("modelUsed", "gpt-4o-mini");

        DocumentHistory doc = new DocumentHistory();
        doc.setId(123);
        Summary mockSummary = new Summary();
        mockSummary.setId(501);
        mockSummary.setDocumentHistory(doc);
        mockSummary.setContent("New summary content.");
        mockSummary.setModelUsed("gpt-4o-mini");

        when(summaryService.create(any(SummaryDTO.class))).thenReturn(mockSummary);

        mockMvc.perform(post("/api/v1/db/summaries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(501))
                .andExpect(jsonPath("$.content").value("New summary content."));
    }

    @Test
    public void testFindById() throws Exception {
        Summary mockSummary = new Summary();
        mockSummary.setId(501);
        mockSummary.setContent("New summary content.");

        when(summaryService.findById(501)).thenReturn(mockSummary);

        mockMvc.perform(get("/api/v1/db/summaries/501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(501))
                .andExpect(jsonPath("$.content").value("New summary content."));
    }

    @Test
    public void testUpdateSummary() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("content", "Updated summary content.");

        Summary mockSummary = new Summary();
        mockSummary.setId(501);
        mockSummary.setContent("Updated summary content.");

        when(summaryService.update(eq(501), any(SummaryDTO.class))).thenReturn(mockSummary);

        mockMvc.perform(patch("/api/v1/db/summaries/501")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(501))
                .andExpect(jsonPath("$.content").value("Updated summary content."));
    }

    @Test
    public void testDeleteSummary() throws Exception {
        doNothing().when(summaryService).deleteById(501);

        mockMvc.perform(delete("/api/v1/db/summaries/501"))
                .andExpect(status().isOk());
    }
}