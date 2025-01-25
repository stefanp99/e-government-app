package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.exceptions.NoComplaintTypeException;
import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.repositories.ComplaintTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class ComplaintTypeService {
    private final ComplaintTypeRepository complaintTypeRepository;

    public ComplaintType findComplaintTypeById(Integer complaintTypeId) throws NoComplaintTypeException {
        return complaintTypeRepository.findById(complaintTypeId).orElseThrow(() -> new NoComplaintTypeException("No Complaint type found"));
    }
}
