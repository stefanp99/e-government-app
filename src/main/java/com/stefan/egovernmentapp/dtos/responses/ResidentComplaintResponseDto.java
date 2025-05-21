package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.ComplaintType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResidentComplaintResponseDto(ComplaintType complaintType,
                                           LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           ComplaintStatus complaintStatus,
                                           String residentNote,
                                           String employeeNote) {
    public static ResidentComplaintResponseDto toDto(Complaint complaint) {
        return ResidentComplaintResponseDto.builder()
                .complaintType(complaint.getComplaintType())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .residentNote(complaint.getResidentNote())
                .employeeNote(complaint.getEmployeeNote())
                .build();
    }
}
