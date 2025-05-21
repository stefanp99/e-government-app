package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.dtos.ResidentDto;
import com.stefan.egovernmentapp.dtos.UserDto;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.ComplaintType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmployeeComplaintResponseDto(ComplaintType complaintType,
                                           LocalDateTime createdAt,
                                           LocalDateTime updatedAt,
                                           ComplaintStatus complaintStatus,
                                           ResidentDto residentDto,
                                           UserDto userDtoModifiedBy,
                                           String residentNote,
                                           String employeeNote) {
    public static EmployeeComplaintResponseDto toDto(Complaint complaint) {
        return EmployeeComplaintResponseDto.builder()
                .complaintType(complaint.getComplaintType())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .complaintStatus(complaint.getComplaintStatus())
                .residentDto(ResidentDto.toDto(complaint.getResident()))
                .userDtoModifiedBy(UserDto.toDto(complaint.getUserModifiedBy()))
                .residentNote(complaint.getResidentNote())
                .employeeNote(complaint.getEmployeeNote())
                .build();
    }
}
