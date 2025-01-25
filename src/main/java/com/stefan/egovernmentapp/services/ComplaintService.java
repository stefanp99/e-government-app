package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.repositories.ComplaintRepository;
import com.stefan.egovernmentapp.repositories.ResidentRepository;
import com.stefan.egovernmentapp.repositories.UserRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.lang.String.format;

@RequiredArgsConstructor

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> addComplaint(String token, Complaint complaint) {
        Optional<Resident> optionalResident = findResidentByToken(token);
        if (optionalResident.isPresent()) {
            complaint.setResident(optionalResident.get());
            complaint.setComplaintStatus(ComplaintStatus.PENDING);
            complaint.setCreatedAt(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(format("Complaint with ID %d was created.", complaintRepository.save(complaint).getId()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resident not found");
    }

    private Optional<Resident> findResidentByToken(String token) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(jwtUtil.extractEmailAddress(token.substring(7)));
        return optionalUser.flatMap(user -> residentRepository.findByUser_Id((user.getId())));
    }
}
