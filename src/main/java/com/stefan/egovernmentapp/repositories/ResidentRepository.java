package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    Optional<Resident> findByPersonalIdNumber(String personalIdNumber);
    Optional<Resident> findByUser_Id(Integer userId);
}
