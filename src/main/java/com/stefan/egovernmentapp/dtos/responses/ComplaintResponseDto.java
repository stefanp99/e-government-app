package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.models.Complaint;
import lombok.Builder;

@Builder
public record ComplaintResponseDto(Integer id,
                                   Integer complaintTypeId,
                                   String complaintStatus,
                                   String note) {
    public static ComplaintResponseDto toDto(Complaint complaint) {
        return ComplaintResponseDto.builder()
                .id(complaint.getId())
                .complaintTypeId(complaint.getComplaintType().getId())
                .complaintStatus(complaint.getComplaintStatus().toString())
                .note(complaint.getResidentNote())
                .build();
    }
}
