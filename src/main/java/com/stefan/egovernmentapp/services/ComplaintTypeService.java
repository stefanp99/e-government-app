package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.repositories.ComplaintTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor

@Service
public class ComplaintTypeService {
    private final ComplaintTypeRepository complaintTypeRepository;

    public ComplaintType findById(Integer complaintTypeId) {
        return complaintTypeRepository.findById(complaintTypeId).orElse(null);
    }

    public ResponseEntity<List<ComplaintType>> findAll() {
        return ResponseEntity.ok(complaintTypeRepository.findAll());
    }
}
