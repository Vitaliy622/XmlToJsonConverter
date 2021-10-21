package com.ssi.controller;

import com.ssi.FileAlreadyExistException;
import com.ssi.service.XmlConverterService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ConverterController {

    private final XmlConverterService xmlConverterService;

    public ConverterController(XmlConverterService xmlConverterService) {
        this.xmlConverterService = xmlConverterService;
    }

    @GetMapping("/")
    public String view() {
        return "fileUploadView";
    }

    @PostMapping("/upload")
    public String submit(@RequestParam("file") MultipartFile file) throws IOException, FileAlreadyExistException {
        xmlConverterService.uploadFile(file);
        return "redirect:/files";
    }

    @GetMapping("/files")
    public String getAllFiles(Model model) throws IOException {
        model.addAttribute("files", xmlConverterService.getAllFiles());
        return "files";
    }

    @GetMapping("/files/{filename}")
    public String getFileContent(@PathVariable String filename, Model model) throws IOException {
        model.addAttribute("file", xmlConverterService.getContent(filename));
        return "file";
    }

    @PostMapping("/files/update/{filename}")
    public String updateFile(@PathVariable String filename, @RequestParam("file") MultipartFile file) {
        xmlConverterService.replaceFile(file, filename);
        return "redirect:/files";
    }

    @PostMapping("/files/delete/{filename}")
    public String deleteFile(@PathVariable String filename, Model model) {
        model.addAttribute("delete", xmlConverterService.deleteFileByName(filename));
        return "redirect:/files";
    }

    @GetMapping("/files/date")
    public String getFilesByDate(Model model, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        List<String> files = xmlConverterService.getFilesByDate(date);
        model.addAttribute("files", files);
        model.addAttribute("date", date);
        return "files";
    }

    @GetMapping("/files/customer")
    public String getFilesByDate(Model model, @RequestParam("customer") String customer) {
        List<String> files = xmlConverterService.getFilesByCustomer(customer);
        model.addAttribute("files", files);
        model.addAttribute("customer", customer);
        return "files";
    }

    @GetMapping("/files/type")
    public String getFilesByDate(@RequestParam("type") String type, Model model) {
        List<String> files = xmlConverterService.getFilesByType(type);
        model.addAttribute("files", files);
        model.addAttribute("type", type);
        return "files";
    }
}
