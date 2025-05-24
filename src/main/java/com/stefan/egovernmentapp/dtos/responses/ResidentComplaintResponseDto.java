package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.models.UploadedDocument;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResidentComplaintResponseDto(ComplaintType complaintType,
                                           LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           ComplaintStatus complaintStatus,
                                           String residentNote,
                                           String employeeNote,
                                           List<String> uploadedDocumentsPaths) {
    public static ResidentComplaintResponseDto toDto(Complaint complaint) {
        return ResidentComplaintResponseDto.builder()
                .complaintType(complaint.getComplaintType())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .residentNote(complaint.getResidentNote())
                .employeeNote(complaint.getEmployeeNote())
                .uploadedDocumentsPaths(complaint.getUploadedDocuments().stream()
                        .map(UploadedDocument::getPath)
                        .toList())
                .build();
    }
}
