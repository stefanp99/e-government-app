package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.ComplaintRequestDto;
import com.stefan.egovernmentapp.services.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                                               @RequestBody ComplaintRequestDto complaintRequestDto) {
        return complaintService.addComplaint(token, complaintRequestDto);
    }

    @PatchMapping("{complaintId}")
    public ResponseEntity<String> updateComplaint(@RequestHeader("Authorization") String token,
                                                  @PathVariable("complaintId") Integer complaintId,
                                                  @RequestBody ComplaintRequestDto complaintRequestDto) {
        return complaintService.updateComplaint(token, complaintId, complaintRequestDto);
    }
}
