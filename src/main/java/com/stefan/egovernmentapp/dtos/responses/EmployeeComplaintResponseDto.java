package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.dtos.ResidentDto;
import com.stefan.egovernmentapp.dtos.UserDto;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.models.UploadedDocument;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EmployeeComplaintResponseDto(Integer id,
                                           ComplaintType complaintType,
                                           LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           ComplaintStatus complaintStatus,
                                           ResidentDto residentDto,
                                           UserDto userDtoModifiedBy,
                                           String residentNote,
                                           String employeeNote,
                                           List<String> uploadedDocumentsPaths) {
    public static EmployeeComplaintResponseDto toDto(Complaint complaint) {
        return EmployeeComplaintResponseDto.builder()
                .id(complaint.getId())
                .complaintType(complaint.getComplaintType())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .residentDto(ResidentDto.toDto(complaint.getResident()))
                .userDtoModifiedBy(complaint.getUserModifiedBy() == null ?
                        null : UserDto.toDto(complaint.getUserModifiedBy()))
                .residentNote(complaint.getResidentNote())
                .employeeNote(complaint.getEmployeeNote())
                .uploadedDocumentsPaths(complaint.getUploadedDocuments().stream()
                        .map(UploadedDocument::getPath)
                        .toList())
                .build();
    }
}
