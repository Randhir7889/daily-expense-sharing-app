package com.example.demo.demo.controller;


import com.example.expense.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/balance-sheet")
    public ResponseEntity<byte[]> downloadBalanceSheet() {
        byte[] content = reportService.downloadBalanceSheet();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=balance_sheet.csv")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(content);
    }
}