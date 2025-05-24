package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.UploadedDocument;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.repositories.ComplaintRepository;
import com.stefan.egovernmentapp.repositories.UploadedDocumentRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class UploadedDocumentService {
    private final UploadedDocumentRepository uploadedDocumentRepository;
    private final ComplaintRepository complaintRepository;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ResponseEntity<String> uploadDocumentsComplaints(String token, MultipartFile file, Integer complaintId) {
        try {
            Optional<User> optionalUser = jwtUtil.findUserByToken(token);
            Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);

            if (optionalUser.isPresent() && optionalComplaint.isPresent()) {
                String filePath = saveImage(file);
                User user = optionalUser.get();
                Complaint complaint = optionalComplaint.get();

                if (complaint.getResident().getUser().equals(user)) {
                    uploadedDocumentRepository.save(UploadedDocument.builder()
                            .user(user)
                            .dateTime(LocalDateTime.now())
                            .path(filePath)
                            .complaint(complaint)
                            .build());

                    return ResponseEntity.ok("Image uploaded successfully: " + filePath);
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Complaint does not belong to user");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload file");
        }
    }


    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        if (!isValidFileName(fileName)) {
            throw new IOException("Invalid file name");
        }
        Path filePath = uploadPath.resolve(fileName).normalize();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    private boolean isValidFileName(String fileName) {
        return fileName != null && fileName.matches("^[a-zA-Z0-9._-]+$");
    }
}
