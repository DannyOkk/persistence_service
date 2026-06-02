package ar.edu.um.fi.ingsr.persistence.controller;

import ar.edu.um.fi.ingsr.persistence.domain.DocumentHistory;
import ar.edu.um.fi.ingsr.persistence.domain.User;
import ar.edu.um.fi.ingsr.persistence.dto.DocumentDTO;
import ar.edu.um.fi.ingsr.persistence.service.DocumentService;
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

@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean                                                             // cambio: @MockBean → @MockitoBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFindAll() throws Exception {
        List<DocumentHistory> list = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        DocumentHistory doc = new DocumentHistory(user, "doc1.pdf", "/path1");
        doc.setId(100);
        list.add(doc);

        when(documentService.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/v1/db/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].filename").value("doc1.pdf"))
                .andExpect(jsonPath("$[0].filePath").value("/path1"));
    }

    @Test
    public void testCreateDocument() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", 1);
        payload.put("filename", "report.pdf");
        payload.put("filePath", "/files/report.pdf");

        User user = new User();
        user.setId(1L);
        DocumentHistory mockDoc = new DocumentHistory(user, "report.pdf", "/files/report.pdf");
        mockDoc.setId(200);

        when(documentService.create(any(DocumentDTO.class))).thenReturn(mockDoc);

        mockMvc.perform(post("/api/v1/db/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.filename").value("report.pdf"));
    }

    @Test
    public void testFindById() throws Exception {
        User user = new User();
        user.setId(1L);
        DocumentHistory mockDoc = new DocumentHistory(user, "report.pdf", "/files/report.pdf");
        mockDoc.setId(200);

        when(documentService.findById(200)).thenReturn(mockDoc);

        mockMvc.perform(get("/api/v1/db/documents/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.filename").value("report.pdf"));
    }

    @Test
    public void testUpdateDocument() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("filename", "updated_report.pdf");

        User user = new User();
        user.setId(1L);
        DocumentHistory mockDoc = new DocumentHistory(user, "updated_report.pdf", "/files/report.pdf");
        mockDoc.setId(200);

        when(documentService.update(eq(200), any(DocumentDTO.class))).thenReturn(mockDoc);

        mockMvc.perform(patch("/api/v1/db/documents/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.filename").value("updated_report.pdf"));
    }

    @Test
    public void testDeleteDocument() throws Exception {
        doNothing().when(documentService).deleteById(200);

        mockMvc.perform(delete("/api/v1/db/documents/200"))
                .andExpect(status().isOk());
    }
}