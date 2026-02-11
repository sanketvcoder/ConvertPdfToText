package com.demo.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.login.service.pdfService;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final pdfService pdfService;

    public PdfController(pdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertPdfToText(
            @RequestParam String folderPath,
            @RequestParam String fileName) {

        try {
            String result = pdfService.convertPdfToText(folderPath, fileName);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error converting PDF: " + e.getMessage());
        }
    }
}
