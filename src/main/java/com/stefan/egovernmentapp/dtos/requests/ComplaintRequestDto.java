package com.stefan.egovernmentapp.dtos.requests;

import lombok.Builder;

@Builder
public record ComplaintRequestDto(Integer complaintTypeId,
                                  String complaintStatus,
                                  String note) {
}
