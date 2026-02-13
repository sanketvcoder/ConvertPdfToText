package com.demo.login.controller;

import java.io.File;
import java.nio.file.Files;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.login.service.FileConversionService;

@RestController
@RequestMapping("/api/convert")
public class FileConversionController {

    private final FileConversionService fileConversionService;

    public FileConversionController(FileConversionService service) {
        this.fileConversionService = service;
    }

    @PostMapping
    public ResponseEntity<byte[]> convert(
            @RequestParam MultipartFile file,
            @RequestParam String targetFormat) {

        try {
            File convertedFile = fileConversionService.convert(file, targetFormat);

            byte[] fileContent = Files.readAllBytes(convertedFile.toPath());

            String originalName = file.getOriginalFilename();
            String newFileName
                    = originalName.substring(0, originalName.lastIndexOf("."))
                    + "." + targetFormat;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + newFileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                    .body(fileContent);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }

}
