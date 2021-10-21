package com.ssi.service;

import com.ssi.FileAlreadyExistException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface XmlConverterService {
    void uploadFile(MultipartFile file) throws IOException, FileAlreadyExistException;

    void replaceFile(MultipartFile file, String pastFilename);

    boolean deleteFileByName(String filename);

    String getContent(String filename) throws IOException;

    List<String> getFilesByDate(Date date);

    List<String> getFilesByCustomer(String name);

    List<String> getFilesByType(String name);

    List<String> getAllFiles() throws IOException;
}
