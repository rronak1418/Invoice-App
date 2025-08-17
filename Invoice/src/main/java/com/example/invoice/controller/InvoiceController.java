package com.example.invoice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.invoice.dto.InvoiceDto;
import com.example.invoice.service.InvoiceService;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:4000")
public class InvoiceController {

    @Autowired
     InvoiceService service;

    @GetMapping("/invoices")
    public ResponseEntity<Page<InvoiceDto>> getInvoices(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getPaginatedInvoices(page, size));
    }

    @PostMapping(path="/upload")
    public ResponseEntity<Map<String, String>> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("message", service.uploadCsv(file));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}