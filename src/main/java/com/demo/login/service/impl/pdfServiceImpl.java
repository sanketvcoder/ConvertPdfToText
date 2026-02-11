package com.demo.login.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.demo.login.service.pdfService;

@Service
public class pdfServiceImpl implements pdfService {

    @Override
    public String convertPdfToText(String folderPath, String fileName) throws IOException {

        System.out.println("Before trim fileName = '" + fileName + "'");

        fileName = fileName.trim();
        folderPath = folderPath.trim();

        System.out.println("After trim fileName = '" + fileName + "'");

        if (!fileName.toLowerCase().endsWith(".pdf")) {
            throw new IOException("File must be a PDF");
        }

        Path pdfPath = Path.of(folderPath, fileName);

        if (!Files.exists(pdfPath)) {
            throw new IOException("File not found: " + pdfPath);
        }

        String txtFileName = fileName.replace(".pdf", ".txt");
        Path txtPath = Path.of(folderPath, txtFileName);

        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Files.writeString(txtPath, text);
        }

        return txtPath.toString();
    }

}
