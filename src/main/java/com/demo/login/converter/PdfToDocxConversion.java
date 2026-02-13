package com.demo.login.converter;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

@Component
public class PdfToDocxConversion implements FileConverter {

    @Override
    public boolean supports(String sourceFormat, String targetFormat) {
        return sourceFormat.equals("pdf") && targetFormat.equals("docx");
    }

    @Override
    public File convert(File inputFile) throws Exception {

        File outputFile = new File(
                inputFile.getParent(),
                inputFile.getName().replace(".pdf", ".docx")
        );

        // Step 1: Extract text from PDF
        String pdfText;
        try (PDDocument document = PDDocument.load(inputFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            pdfText = stripper.getText(document);
        }

        // Step 2: Write text into DOCX
        try (XWPFDocument doc = new XWPFDocument()) {

            String[] lines = pdfText.split("\\r?\\n");

            for (String line : lines) {
                XWPFParagraph paragraph = doc.createParagraph();
                paragraph.createRun().setText(line);
            }

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                doc.write(fos);
            }
        }

        return outputFile;
    }
}
