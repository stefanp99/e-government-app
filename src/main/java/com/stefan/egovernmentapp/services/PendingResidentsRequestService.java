package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.models.PendingResidentsRequest;
import com.stefan.egovernmentapp.repositories.PendingResidentsRequestRepository;
import com.stefan.egovernmentapp.repositories.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.stefan.egovernmentapp.models.RequestStatus.ACCEPTED;
import static com.stefan.egovernmentapp.models.RequestStatus.PENDING;
import static com.stefan.egovernmentapp.models.RequestStatus.REJECTED;
import static com.stefan.egovernmentapp.utils.EmailUtil.BODY_PENDING_RESIDENT_REQUEST;
import static com.stefan.egovernmentapp.utils.EmailUtil.SUBJECT_PENDING_RESIDENT_REQUEST;
import static java.lang.String.format;

@RequiredArgsConstructor

@Service
public class PendingResidentsRequestService {
    private final PendingResidentsRequestRepository pendingResidentsRequestRepository;
    private final ResidentRepository residentRepository;
    private final EmailService emailService;

    public ResponseEntity<List<PendingResidentsRequest>> getPendingResidentsRequestsWithStatusPending() {
        return ResponseEntity.ok(pendingResidentsRequestRepository.findAllByRequestStatus(PENDING));
    }

    public ResponseEntity<String> addPendingResidentsRequests(PendingResidentsRequest pendingResidentsRequest) {
        if (!residentExistsByPersonalIdNumber(pendingResidentsRequest.getPersonalIdNumber()) &&
                !requestExistsByPersonalIdNumber(pendingResidentsRequest.getPersonalIdNumber())) {
            pendingResidentsRequest.setRequestStatus(PENDING);
            pendingResidentsRequest.setCreatedAt(LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(format("Request was added with ID %d",
                            pendingResidentsRequestRepository.save(pendingResidentsRequest).getId()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request for this resident already exists");
    }

    public ResponseEntity<String> approveOrRejectRequest(Integer requestId, String action) {
        Optional<PendingResidentsRequest> optionalPendingResidentsRequest =
                pendingResidentsRequestRepository.findByIdAndRequestStatus(requestId, PENDING);
        if (optionalPendingResidentsRequest.isPresent()) {
            PendingResidentsRequest pendingResidentsRequest = optionalPendingResidentsRequest.get();
            if (action.equals("approve"))
                pendingResidentsRequest.setRequestStatus(ACCEPTED);
            else if (action.equals("reject"))
                pendingResidentsRequest.setRequestStatus(REJECTED);
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Action not recognized");
            pendingResidentsRequestRepository.save(pendingResidentsRequest);

            emailService.sendSimpleEmail(pendingResidentsRequest.getEmailAddress(),
                    SUBJECT_PENDING_RESIDENT_REQUEST,
                    String.format(BODY_PENDING_RESIDENT_REQUEST, action + "d"));

            return ResponseEntity.status(HttpStatus.OK).body(format("Request was modified with action %s", action));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(format("Request with ID %d does not exist or is not pending", requestId));
    }

    public PendingResidentsRequest getPendingResidentsRequestByEmailAddress(String emailAddress) {
        return pendingResidentsRequestRepository.findByEmailAddress(emailAddress).orElse(null);
    }

    private boolean residentExistsByPersonalIdNumber(String personalIdNumber) {
        return residentRepository.findByPersonalIdNumber(personalIdNumber).isPresent();
    }

    private boolean requestExistsByPersonalIdNumber(String personalIdNumber) {
        return pendingResidentsRequestRepository.findByPersonalIdNumber(personalIdNumber).isPresent();
    }
}
