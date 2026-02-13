package com.demo.login.converter;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class DocxToPdfConversion implements FileConverter {

    private final DocxToTextConverter docxToTextConverter;
    private final TextToPdfConverter textToPdfConverter;

    public DocxToPdfConversion(DocxToTextConverter docxToTextConverter, TextToPdfConverter textToPdfConverter) {
        this.docxToTextConverter = docxToTextConverter;
        this.textToPdfConverter = textToPdfConverter;
    }

    @Override
    public boolean supports(String sourceFormat, String targetFormat) {
        return sourceFormat.equals("docx") && targetFormat.equals("pdf");
    }

    @Override
    public File convert(File inputFile) throws Exception {
        File intermediateFile = docxToTextConverter.convert(inputFile);
        return textToPdfConverter.convert(intermediateFile);
    }

}
