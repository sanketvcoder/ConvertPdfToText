package com.demo.login.converter;

import java.io.File;

public interface FileConverter {

    boolean supports(String sourceFormat, String targetFormat);

    File convert(File inputFile) throws Exception;
}
