package com.stefan.egovernmentapp.dtos;

import com.stefan.egovernmentapp.models.ComplaintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDto {
    private Integer id;
    private String information;
    private ComplaintType complaintType;
    private Timestamp addedDate;
    private String status;
}
