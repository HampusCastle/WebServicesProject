package hampusborg.webservicesproject.service;

import hampusborg.webservicesproject.exception.PhotoUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService {

    private static final String PHOTO_DIRECTORY = "photos";

    public String savePhoto(String id, MultipartFile file) {
        String filename = id + getFileExtension(file.getOriginalFilename());
        Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
        try {
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(filename));
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/" + filename)
                    .toUriString();
        } catch (IOException exception) {
            throw new PhotoUploadException("Unable to save photo: " + exception.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        return filename.contains(".") ? filename.substring(filename.lastIndexOf(".")) : ".png";
    }
}