package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.AddedComplaintResidentDto;
import com.stefan.egovernmentapp.dtos.ComplaintDto;
import com.stefan.egovernmentapp.exceptions.NoComplaintException;
import com.stefan.egovernmentapp.exceptions.NoComplaintTypeException;
import com.stefan.egovernmentapp.exceptions.NoKnownStatusException;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.repositories.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor

@Service
public class ComplaintService {
    private final ComplaintTypeService complaintTypeService;
    private final ResidentService residentService;
    private final ComplaintRepository complaintRepository;

    public Complaint addComplaint(AddedComplaintResidentDto addedComplaintDto) {
        try {
            ComplaintType complaintType = complaintTypeService.findComplaintTypeById(addedComplaintDto.getComplaintTypeId());
            Resident resident = residentService.addResidentIfNotExists(addedComplaintDto.getResidentName(),
                    addedComplaintDto.getResidentEmail(),
                    addedComplaintDto.getResidentAddress());
            Complaint complaint = Complaint
                    .builder()
                    .addedDate(new Timestamp(System.currentTimeMillis()))
                    .complaintType(complaintType)
                    .information(addedComplaintDto.getComplaintInformation())
                    .resident(resident)
                    .status("Open")
                    .build();
            return complaintRepository.save(complaint);
        } catch (NoComplaintTypeException e) {
            throw new RuntimeException(e);
        }

    }

    public ComplaintDto updateComplaintStatus(Integer complaintId, String newStatus) throws NoComplaintException {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NoComplaintException("No complaint found with this ID"));
        if (complaint != null) {
            ExpressionParser parser = new SpelExpressionParser();
            parser.parseExpression("status").setValue(complaint, newStatus);
            complaintRepository.save(complaint);
            return new ComplaintDto(complaint.getId(), complaint.getInformation(), complaint.getComplaintType(),
                    complaint.getAddedDate(), complaint.getStatus());
        }
        return null;
    }

    public List<ComplaintDto> findComplaintsByStatus(String status) throws NoKnownStatusException {
        if (!Arrays.asList(new String[]{"Open", "Resolved", "Pending", "Accepted", "Rejected"}).contains(status))
            throw new NoKnownStatusException("Status is not known");
        List<Complaint> complaints = complaintRepository.findByStatus(status);
        List<ComplaintDto> complaintDtos = new ArrayList<>();
        complaints.forEach(complaint -> complaintDtos.add(new ComplaintDto(complaint.getId(), complaint.getInformation(), complaint.getComplaintType(),
                complaint.getAddedDate(), complaint.getStatus())));

        return complaintDtos;
    }
}
