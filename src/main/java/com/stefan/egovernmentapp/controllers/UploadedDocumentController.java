package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.services.UploadedDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor

@RestController
@RequestMapping("upload-documents")
public class UploadedDocumentController {
    private final UploadedDocumentService uploadedDocumentService;

    @PostMapping("complaints")
    public ResponseEntity<String> uploadDocumentsComplaints(@RequestHeader("Authorization") String token,
                                                  @RequestParam("file") MultipartFile file,
                                                  @RequestParam Integer complaintId) {
        return uploadedDocumentService.uploadDocumentsComplaints(token, file, complaintId);
    }
}
