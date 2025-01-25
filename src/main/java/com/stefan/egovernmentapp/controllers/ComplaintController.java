package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.models.Complaint;
import com.stefan.egovernmentapp.services.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("complaints")
@RestController
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<String> addComplaint(@RequestHeader("Authorization") String token,
                                               @RequestBody Complaint complaint) {
        return complaintService.addComplaint(token, complaint);
    }
}
