package com.demo.login.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileConversionService {

    File convert(MultipartFile file, String targetFormat) throws Exception;
}
