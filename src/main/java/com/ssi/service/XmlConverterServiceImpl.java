package com.ssi.service;

import com.ssi.FileAlreadyExistException;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class XmlConverterServiceImpl implements XmlConverterService {

    public static final String UPLOAD_DIRECTORY = "/uploads/";
    public static final String FILENAME_PATTERN = "^[a-z]{1,10}[_][a-z]{1,10}[_][0-9]{4}([-])(((0[13578]|(10|12))\\1(0[1-9]|[1-2][0-9]|3[0-1]))|(02\\1(0[1-9]|[1-2][0-9]))|((0[469]|11)\\1(0[1-9]|[1-2][0-9]|30)))\\.xml$";

    @Override
    public void uploadFile(MultipartFile file) {
        String jsonFile = UPLOAD_DIRECTORY + FilenameUtils.removeExtension(file.getOriginalFilename()) + ".json";
        File f = new File(jsonFile);
        try {
            if (checkIfFilenameMatchesToPattern(file) && checkIfFileExist(f)) {
                writeJsonFile(file, jsonFile);
            }
        } catch (FileAlreadyExistException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfFileExist(File file) throws FileAlreadyExistException {
        if (!file.exists()) return true;
        else throw new FileAlreadyExistException("File Already Exist " + file.getName());
    }

    public void writeJsonFile(MultipartFile file, String jsonFile) {
        try {
            JSONObject xmlJSONObj = convertFile(file);
            try (FileWriter fileWriter = new FileWriter(jsonFile)) {
                int prettyPrintIndentFactor = 4;
                fileWriter.write(xmlJSONObj.toString(prettyPrintIndentFactor));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject convertFile(MultipartFile file) throws IOException {
        String xmlFile = new String(file.getBytes());
        return XML.toJSONObject(xmlFile);
    }

    public boolean checkIfFilenameMatchesToPattern(MultipartFile file) {
        Pattern pattern = Pattern.compile(FILENAME_PATTERN);
        Matcher m = pattern.matcher(Objects.requireNonNull(file.getOriginalFilename()));
        return m.matches();
    }


    @Override
    public void replaceFile(MultipartFile file, String pastFilename) {
        pastFilename = UPLOAD_DIRECTORY + pastFilename;
        boolean matches = checkIfFilenameMatchesToPattern(file);
        if (matches) {
            writeJsonFile(file, pastFilename);
        }
    }

    @Override
    public boolean deleteFileByName(String filename) {
        filename = UPLOAD_DIRECTORY + filename;

        try {
            File file = new File(filename);
            if (file.delete()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getContent(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(UPLOAD_DIRECTORY + filename)));
    }

    @Override
    public List<String> getFilesByDate(Date date) {
        List<String> files = getAllFiles();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);
        files.removeIf(file -> !file.matches("^[a-z]{1,10}[_][a-z]{1,10}[_]" + strDate + ".json$"));
        return files;
    }

    @Override
    public List<String> getFilesByCustomer(String name) {
        List<String> files = getAllFiles();
        files.removeIf(file -> !file.matches("^" + name + "{1,10}" +
                "[_][a-z]{1,10}[_]" +
                "[0-9]{4}([-])(((0[13578]|(10|12))\\1(0[1-9]|[1-2][0-9]|3[0-1]))|(02\\1(0[1-9]|[1-2][0-9]))|((0[469]|11)\\1(0[1-9]|[1-2][0-9]|30)))" +
                "\\.json$"));
        return files;
    }

    @Override
    public List<String> getFilesByType(String name) {
        List<String> files = getAllFiles();
        files.removeIf(file -> !file.matches("^[a-z]{1,10}" +
                "[_]" + name + "{1,10}" +
                "[_]" +
                "[0-9]{4}([-])(((0[13578]|(10|12))\\1(0[1-9]|[1-2][0-9]|3[0-1]))|(02\\1(0[1-9]|[1-2][0-9]))|((0[469]|11)\\1(0[1-9]|[1-2][0-9]|30)))" +
                "\\.json$"));
        return files;
    }

    @Override
    public List<String> getAllFiles() {
        List<String> results = new ArrayList<>();

        File[] files = new File(UPLOAD_DIRECTORY).listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }
}