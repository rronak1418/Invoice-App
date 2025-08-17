package com.example.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDto {
    private String customerId;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private String description;
    private BigDecimal amount;
    private long ageInDays;

    public InvoiceDto() {}
    public InvoiceDto(String customerId, String invoiceNumber, LocalDate invoiceDate, String description, BigDecimal amount, long ageInDays) {
        this.customerId = customerId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.description = description;
        this.amount = amount;
        this.ageInDays = ageInDays;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getAgeInDays() {
        return ageInDays;
    }

    public void setAgeInDays(long ageInDays) {
        this.ageInDays = ageInDays;
    }
    
}