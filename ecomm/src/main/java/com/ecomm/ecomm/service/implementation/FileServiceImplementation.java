package com.ecomm.ecomm.service.implementation;

import com.ecomm.ecomm.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImplementation implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        // File names of current/original file
        String originalFileName = image.getOriginalFilename();

        // Generate a unique file name (UUID)
        // image.jpg --> 123 --> 123.jpg
        String randomId = UUID.randomUUID().toString();
        String fileName = null;
        if (originalFileName != null) {
            fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        }
        String filePath = path + File.pathSeparator + fileName;
        //Check if path exists and create
        File folder = new File(path);
        if(!(folder.exists())){
            folder.mkdir();
        }
        if (!folder.canWrite()) {
            throw new IOException("No write permissions for: " + path);
        }
        // Upload to server
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
