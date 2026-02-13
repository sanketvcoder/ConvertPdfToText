package com.demo.login.converter;

import java.io.File;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PdfToTextConverter implements FileConverter {

    @Override
    public boolean supports(String source, String target) {
        return source.equals("pdf") && target.equals("txt");
    }

    @Override
    public File convert(File inputFile) throws Exception {
        File outputFile = new File(inputFile.getParent(), inputFile.getName().replace("pdf", "txt"));

        try (PDDocument document = PDDocument.load(inputFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Files.writeString(outputFile.toPath(), text);
        }

        return outputFile;
    }

}
