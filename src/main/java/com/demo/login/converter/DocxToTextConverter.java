package com.demo.login.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

@Component
public class DocxToTextConverter implements FileConverter {

    @Override
    public boolean supports(String source, String target) {
        return source.equals("docx") && target.equals("txt");
    }

    @Override
    public File convert(File inputFile) throws Exception {

        File outputFile = new File(inputFile.getParent(),
                inputFile.getName().replace(".docx", ".txt"));

        try (XWPFDocument document = new XWPFDocument(new FileInputStream(inputFile))) {

            StringBuilder text = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(text.toString().getBytes());
            }
        }

        return outputFile;
    }
}
