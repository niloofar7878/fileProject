package com.example.filePro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;


@Service
public class FileStorageService {
    @Value("${storage.location}")
    private String storageLocation;

    private final ResourceLoader resourceLoader;

    public FileStorageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void saveFile(MultipartFile multipartFile, String filePath) throws IOException {
        File file= new File(storageLocation + "//" +filePath + multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream= new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
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

    public void downloadFile(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource("file:" + filePath);
        InputStream inputStream = resource.getInputStream();
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\AsusIran\\OneDrive\\Desktop\\download");

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();
    }

    public InputStream loadLargeFile() throws IOException {
        File file = new File( "C:\\Users\\AsusIran\\OneDrive\\Desktop\\save-file\\nullch10.pdf");
        InputStream inputStream = new DataInputStream(new FileInputStream(file));
        return inputStream;
    }

    public ResponseEntity<InputStreamResource> downloadLargeFile(MultipartFile multipartFile)
            throws Exception {
        final HttpHeaders httpHeaders = new HttpHeaders();
        final File file = new File("C:\\Users\\AsusIran\\OneDrive\\Desktop\\download");
        final InputStream inputStream = new FileInputStream(file);
        final InputStreamResource resource = new InputStreamResource(inputStream);
        httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}
