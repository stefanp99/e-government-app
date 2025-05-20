package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.PendingResidentsRequest;
import com.stefan.egovernmentapp.models.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingResidentsRequestRepository extends JpaRepository<PendingResidentsRequest, Integer> {
    List<PendingResidentsRequest> findAllByRequestStatus(RequestStatus requestStatus);

    Optional<PendingResidentsRequest> findByIdAndRequestStatus(Integer id, RequestStatus requestStatus);

    Optional<PendingResidentsRequest> findByEmailAddressAndRequestStatus(String emailAddress, RequestStatus requestStatus);

    Optional<PendingResidentsRequest> findByPersonalIdNumber(String personalIdNumber);

    Optional<PendingResidentsRequest> findByEmailAddress(String emailAddress);
}
