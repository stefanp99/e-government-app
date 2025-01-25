package com.stefan.egovernmentapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddedComplaintResidentDto {
    private String complaintInformation;
    private Integer complaintTypeId;
    private String residentName;
    private String residentEmail;
    private String residentAddress;
}
