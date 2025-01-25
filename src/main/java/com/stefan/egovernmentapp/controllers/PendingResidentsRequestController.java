package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.models.PendingResidentsRequest;
import com.stefan.egovernmentapp.services.PendingResidentsRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("pending-residents-requests")
public class PendingResidentsRequestController {
    private final PendingResidentsRequestService pendingResidentsRequestService;

    @GetMapping
    public ResponseEntity<List<PendingResidentsRequest>> getPendingResidentsRequests() {
        return pendingResidentsRequestService.getPendingResidentsRequests();
    }

    @PostMapping
    public ResponseEntity<String> addPendingResidentsRequests(@RequestBody PendingResidentsRequest pendingResidentsRequest) {
        return pendingResidentsRequestService.addPendingResidentsRequests(pendingResidentsRequest);
    }

    @PatchMapping("{requestId}/{action}")
    public ResponseEntity<String> approveOrRejectPendingResidentsRequest(@PathVariable Integer requestId, @PathVariable String action) {
        return pendingResidentsRequestService.approveOrRejectRequest(requestId, action);
    }
}
