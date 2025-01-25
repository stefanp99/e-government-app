package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {
    long countByNameAndEmailAndAddress(String name, String email, String address);

    Resident findByNameAndEmailAndAddress(String residentName, String residentEmail, String residentAddress);
}
