package com.example.invoice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.example.invoice.entity.Invoice;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.invoice.dto.InvoiceDto;
import com.example.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository repository;

    public String uploadCsv(MultipartFile file) throws Exception {
        List<Invoice> invoices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (var record : csvParser.getRecords()) {
                String customerId = record.get("Customer ID");
                String invoiceNumber = record.get("Invoice Number");

                if (repository.existsByCustomerIdAndInvoiceNumber(customerId, invoiceNumber)) {
                    throw new IllegalArgumentException("Customer ID or Invoice Number already exists");
                }
                Invoice invoice = new Invoice();
                invoice.setCustomerId(customerId);
                invoice.setInvoiceNumber(invoiceNumber);
                invoice.setInvoiceDate(LocalDate.parse(record.get("Invoice Date")));
                invoice.setDescription(record.get("Description"));
                invoice.setAmount(new BigDecimal(record.get("Amount")));
                invoices.add(invoice);
            }
            if(invoices.isEmpty()) {
                throw new Exception("No invoices were found");
            }
            repository.saveAll(invoices);
            return "Uploaded " + invoices.size() + " records";
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid Invoice Date format: ");
        }catch (Exception e) {
            throw new Exception("Error processing CSV: " + e.getMessage(), e);
        }
    }


    public Page<InvoiceDto> getPaginatedInvoices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate today = LocalDate.now();
        return repository.findAll(pageable).map(invoice -> {
            long age = ChronoUnit.DAYS.between(invoice.getInvoiceDate(), today);
            return new InvoiceDto(
                    invoice.getCustomerId(),
                    invoice.getInvoiceNumber(),
                    invoice.getInvoiceDate(),
                    invoice.getDescription(),
                    invoice.getAmount(),
                    age
            );
        });
    }
}