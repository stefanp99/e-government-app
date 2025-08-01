package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.requests.ComplaintRequestDto;
import com.stefan.egovernmentapp.dtos.responses.ComplaintResponseDto;
import com.stefan.egovernmentapp.dtos.responses.EmployeeComplaintResponseDto;
import com.stefan.egovernmentapp.dtos.responses.ResidentComplaintResponseDto;
import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.models.ComplaintStatus;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.repositories.ComplaintRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.stefan.egovernmentapp.utils.EmailUtil.BODY_COMPLAINT_RECEIVED;
import static com.stefan.egovernmentapp.utils.EmailUtil.BODY_COMPLAINT_STATUS_CHANGED;
import static com.stefan.egovernmentapp.utils.EmailUtil.SUBJECT_COMPLAINT_RECEIVED;
import static com.stefan.egovernmentapp.utils.EmailUtil.SUBJECT_COMPLAINT_STATUS_CHANGED;

@RequiredArgsConstructor

@Service
public class ComplaintService {
    private final ComplaintTypeService complaintTypeService;
    private final ComplaintRepository complaintRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public ResponseEntity<ComplaintResponseDto> addComplaint(String token, ComplaintRequestDto complaintRequestDto) {
        Optional<Resident> optionalResident = jwtUtil.findResidentByToken(token);
        if (optionalResident.isPresent()) {
            Resident resident = optionalResident.get();
            Complaint complaint = Complaint.builder()
                    .complaintType(complaintTypeService.findById(complaintRequestDto.complaintTypeId()))
                    .createdAt(LocalDateTime.now())
                    .complaintStatus(ComplaintStatus.PENDING)
                    .resident(resident)
                    .residentNote(complaintRequestDto.note())
                    .build();
            Complaint createdComplaint = complaintRepository.save(complaint);

            emailService.sendSimpleEmail(resident.getUser().getEmailAddress(),
                    String.format(SUBJECT_COMPLAINT_RECEIVED, createdComplaint.getId()),
                    String.format(BODY_COMPLAINT_RECEIVED,
                            createdComplaint.getComplaintType().getTypeName(), createdComplaint.getResidentNote()));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ComplaintResponseDto.toDto(complaint));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    public ResponseEntity<String> updateComplaint(String token, Integer complaintId, ComplaintRequestDto complaintRequestDto) {
        Optional<User> optionalUser = jwtUtil.findUserByToken(token);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);
        if (optionalComplaint.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Complaint not found");
        }

        User user = optionalUser.get();

        Complaint complaint = optionalComplaint.get();
        complaint.setComplaintStatus(ComplaintStatus.valueOf(complaintRequestDto.complaintStatus()));
        complaint.setEmployeeNote(complaintRequestDto.note());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaint.setUserModifiedBy(user);

        complaintRepository.save(complaint);

        emailService.sendSimpleEmail(complaint.getResident().getUser().getEmailAddress(),
                String.format(SUBJECT_COMPLAINT_STATUS_CHANGED, complaint.getId()),
                String.format(BODY_COMPLAINT_STATUS_CHANGED,
                        complaint.getComplaintStatus().toString(), complaint.getEmployeeNote()));

        return ResponseEntity.ok(String.format("Complaint with ID %d was updated.", complaintId));
    }

    public ResponseEntity<List<ResidentComplaintResponseDto>> findResidentComplaints(String token) {
        Optional<Resident> optionalResident = jwtUtil.findResidentByToken(token);
        return optionalResident.map(resident -> ResponseEntity.ok(resident.getComplaints().stream()
                        .map(ResidentComplaintResponseDto::toDto)
                        .toList()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList()));
    }

    public ResponseEntity<List<EmployeeComplaintResponseDto>> findEmployeeComplaints() {
        return ResponseEntity.ok(complaintRepository.findAll().stream()
                .map(EmployeeComplaintResponseDto::toDto)
                .toList());
    }
}
