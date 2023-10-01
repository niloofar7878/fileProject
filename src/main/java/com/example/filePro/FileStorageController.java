package com.example.filePro;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class FileStorageController {
    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping( value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void writeFile(@RequestParam("file") MultipartFile file,
                          @RequestParam(required = false) String ignoredFilePath)
    throws Exception {
        fileStorageService.saveFile(file, ignoredFilePath);
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

    @GetMapping("/download/{filePath}")
    public void downloadFile(@PathVariable String filePath) throws IOException {
        fileStorageService.downloadFile(filePath);
    }

    @GetMapping("/download/large")
    public void loadLarge() throws IOException {
       fileStorageService.loadLargeFile();
    }
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam MultipartFile file)
            throws Exception {
        return fileStorageService.downloadLargeFile(file);
    }

}
