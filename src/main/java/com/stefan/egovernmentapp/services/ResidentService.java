package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.ComplaintDto;
import com.stefan.egovernmentapp.exceptions.NoResidentFoundException;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.repositories.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;

    public Resident addResidentIfNotExists(String residentName, String residentEmail, String residentAddress) {
        if (residentRepository.countByNameAndEmailAndAddress(residentName, residentEmail, residentAddress) == 0) {
            Resident resident = Resident.builder()
                    .name(residentName)
                    .email(residentEmail)
                    .address(residentAddress)
                    .build();
            return residentRepository.save(resident);
        } else return residentRepository.findByNameAndEmailAndAddress(residentName, residentEmail, residentAddress);
    }

    @Async
    public CompletableFuture<List<ComplaintDto>> findComplaintsByResidentId(Integer residentId) throws NoResidentFoundException {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new NoResidentFoundException("No Resident found for given ID"));
        List<ComplaintDto> complaintDtos = new ArrayList<>();
        List<Complaint> complaints = resident.getComplaints();
        complaints.forEach(complaint -> {
            complaintDtos.add(ComplaintDto.builder()
                    .id(complaint.getId())
                    .information(complaint.getInformation())
                    .complaintType(complaint.getComplaintType())
                    .addedDate(complaint.getAddedDate())
                    .status(complaint.getStatus())
                    .build());
        });
        return CompletableFuture.completedFuture(complaintDtos);
    }
}
