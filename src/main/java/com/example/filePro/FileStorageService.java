package com.example.filePro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileStorageService {
    @Value("${storage.location}")
    private String storageLocation;

    public String saveFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = storageLocation + "/" + fileName;
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    public byte[] getFile(String fileName) {
        try {
            String filePath = storageLocation + "/" + fileName;
            File file = new File(filePath);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new FileStorageException("Failed to retrieve file", e);
        }
    }

    public void createDirectory(String directoryName) {
        String directoryPath = storageLocation + "/" + directoryName;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully");
            } else {
                throw new FileStorageException("Failed to create directory");
            }
        }
    }


    public void deleteAllFiles() {
        File directory = new File(storageLocation);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }

}
