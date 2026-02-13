package com.demo.login.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.login.converter.FileConverter;
import com.demo.login.service.FileConversionService;

@Service
public class FileConversionServiceImpl implements FileConversionService {

    private static final String OUTPUT_DIRECTORY
            = "C:\\Users\\SANKIT\\Desktop\\All Files";

    private final List<FileConverter> converters;

    public FileConversionServiceImpl(List<FileConverter> converters) {
        this.converters = converters;
    }

    @Override
    public File convert(MultipartFile file, String targetFormat) throws Exception {

        String originalName = file.getOriginalFilename();

        if (originalName == null || !originalName.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String sourceFormat = getExtension(originalName);

        // Create directory if not exists
        File directory = new File(OUTPUT_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save uploaded file in Desktop folder
        File savedInputFile = new File(directory, originalName);
        file.transferTo(savedInputFile);

        // Find correct converter
        for (FileConverter converter : converters) {

            if (converter.supports(sourceFormat, targetFormat.toLowerCase())) {

                File convertedFile = converter.convert(savedInputFile);

                // Rename to original filename with new extension
                String newFileName
                        = originalName.substring(0,
                                originalName.lastIndexOf("."))
                        + "." + targetFormat;

                File finalFile = new File(directory, newFileName);

                // Move & replace if exists
                Files.move(convertedFile.toPath(),
                        finalFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                return finalFile;
            }
        }

        throw new UnsupportedOperationException(
                "Conversion not supported: " + sourceFormat + " → " + targetFormat);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
