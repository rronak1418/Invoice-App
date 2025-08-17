package com.example.Invoice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class InvoiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetInvoices() {
        ResponseEntity<String> response = restTemplate.getForEntity("/invoices?page=0&size=10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("content");
    }

    @Test
    void testUploadCsvSuccess() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv",
                "Customer Id,Invoice Number,Invoice Date,Description,Amount\nCUST007,INV007,2025-08-14,Consulting Services,500.0".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testUploadCsvFailure() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.csv", "text/csv",
                "not,a,valid,csv".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testUploadCsvEmptyField() throws Exception {

        //Testing it with two empty fields (Description and Amount)
        MockMultipartFile file = new MockMultipartFile(
                "file", "invoices.csv", "text/csv",
                "Customer Id,Invoice Number,Invoice Date,Description,Amount\nCUST001,INV001,2025-08-14".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
