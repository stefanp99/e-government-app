package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.ComplaintRequestDto;
import com.stefan.egovernmentapp.dtos.responses.ComplaintResponseDto;
import com.stefan.egovernmentapp.dtos.responses.EmployeeComplaintResponseDto;
import com.stefan.egovernmentapp.dtos.responses.ResidentComplaintResponseDto;
import com.stefan.egovernmentapp.models.ComplaintType;
import com.stefan.egovernmentapp.services.ComplaintService;
import com.stefan.egovernmentapp.services.ComplaintTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("complaints")
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;
    private final ComplaintTypeService complaintTypeService;

    @PostMapping
    public ResponseEntity<ComplaintResponseDto> addComplaint(@RequestHeader("Authorization") String token,
                                                             @RequestBody ComplaintRequestDto complaintRequestDto) {
        return complaintService.addComplaint(token, complaintRequestDto);
    }

    @PatchMapping("{complaintId}")
    public ResponseEntity<String> updateComplaint(@RequestHeader("Authorization") String token,
                                                  @PathVariable("complaintId") Integer complaintId,
                                                  @RequestBody ComplaintRequestDto complaintRequestDto) {
        return complaintService.updateComplaint(token, complaintId, complaintRequestDto);
    }

    @GetMapping("resident")
    public ResponseEntity<List<ResidentComplaintResponseDto>> getResidentComplaints(
            @RequestHeader("Authorization") String token) {
        return complaintService.findResidentComplaints(token);
    }

    @GetMapping("employee")
    public ResponseEntity<List<EmployeeComplaintResponseDto>> getEmployeeComplaints() {
        return complaintService.findEmployeeComplaints();
    }

    @GetMapping("types")
    public ResponseEntity<List<ComplaintType>> getComplaintTypes() {
        return complaintTypeService.findAll();
    }
}
