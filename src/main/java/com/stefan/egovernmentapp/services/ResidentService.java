package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.ResidentDto;
import com.stefan.egovernmentapp.models.PendingResidentsRequest;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.Role;
import com.stefan.egovernmentapp.models.User;
import com.stefan.egovernmentapp.repositories.PendingResidentsRequestRepository;
import com.stefan.egovernmentapp.repositories.ResidentRepository;
import com.stefan.egovernmentapp.repositories.UserRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.stefan.egovernmentapp.models.RequestStatus.ACCEPTED;

@RequiredArgsConstructor

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final UserRepository userRepository;
    private final PendingResidentsRequestRepository pendingResidentsRequestRepository;
    private final JwtUtil jwtUtil;

    public Resident createResidentIfRoleIsResident(String emailAddress) {
        Optional<User> optionalUser = userRepository.findByEmailAddress(emailAddress);
        if (optionalUser.isPresent()) {
            User createdUser = optionalUser.get();
            if (createdUser.getRole().equals(Role.RESIDENT)) {
                Optional<PendingResidentsRequest> optionalPendingResidentsRequest =
                        pendingResidentsRequestRepository
                                .findByEmailAddressAndRequestStatus(emailAddress, ACCEPTED);
                if (optionalPendingResidentsRequest.isPresent()) {
                    PendingResidentsRequest pendingResidentsRequest = optionalPendingResidentsRequest.get();
                    return residentRepository
                            .save(Resident.builder()
                                    .address(pendingResidentsRequest.getAddress())
                                    .name(pendingResidentsRequest.getName())
                                    .user(createdUser)
                                    .address(pendingResidentsRequest.getAddress())
                                    .personalIdNumber(pendingResidentsRequest.getPersonalIdNumber())
                                    .build());
                }
            }
        }
        return null;
    }

    public ResponseEntity<ResidentDto> getCurrentResident(String token) {
        Optional<Resident> optionalResident = jwtUtil.findResidentByToken(token);
        return optionalResident
                .map(resident -> ResponseEntity.ok(ResidentDto.toDto(resident)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
