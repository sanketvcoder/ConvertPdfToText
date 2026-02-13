package com.demo.login.converter;

import java.io.File;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

@Component
public class TextToPdfConverter implements FileConverter {

    @Override
    public boolean supports(String sourceFormat, String targetFormat) {
        return sourceFormat.equals("txt") && targetFormat.equals("pdf");
    }

    @Override
    public File convert(File inputFile) throws Exception {

        File outputFile = new File(inputFile.getParent(),
                inputFile.getName().replace(".txt", ".pdf"));

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream
                    = new PDPageContentStream(document, page);

            PDType1Font font = PDType1Font.HELVETICA;
            float fontSize = 12;
            float margin = 40;
            float yStart = 750;

            float pageWidth = page.getMediaBox().getWidth();
            float usableWidth = pageWidth - 2 * margin;

            contentStream.setFont(font, fontSize);

            String text = Files.readString(inputFile.toPath());
            String[] paragraphs = text.split("\\r?\\n");

            float yPosition = yStart;

            for (String paragraph : paragraphs) {

                String[] words = paragraph.split(" ");
                StringBuilder line = new StringBuilder();

                for (String word : words) {

                    String testLine = line + word + " ";
                    float textWidth
                            = font.getStringWidth(testLine) / 1000 * fontSize;

                    if (textWidth > usableWidth) {

                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(line.toString());
                        contentStream.endText();

                        yPosition -= 15;
                        line = new StringBuilder(word + " ");

                    } else {
                        line.append(word).append(" ");
                    }
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(line.toString());
                contentStream.endText();

                yPosition -= 20;
            }

            contentStream.close();
            document.save(outputFile);
        }

        return outputFile;
    }

}
