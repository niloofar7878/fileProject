package com.example.filePro;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileStorageController {
    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.saveFile(file);
        return "File uploaded successfully. Path: " + filePath;
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        byte[] fileBytes = fileStorageService.getFile(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok().headers(headers).body(fileBytes);
    }

    @PostMapping("/createDirectory")
    public String createDirectory(@RequestParam("directoryName") String directoryName) {
        fileStorageService.createDirectory(directoryName);
        return "Directory created successfully";
    }

    @PostMapping("/deleteAllFiles")
    public String deleteAllFiles() {
        fileStorageService.deleteAllFiles();
        return "All files deleted successfully";
    }

}
