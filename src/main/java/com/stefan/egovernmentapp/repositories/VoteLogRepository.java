package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.VoteLog;
import com.stefan.egovernmentapp.models.embeddables.VoteLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteLogRepository extends JpaRepository<VoteLog, VoteLogId> {
    Optional<VoteLog> findByResident(Resident resident);
}
