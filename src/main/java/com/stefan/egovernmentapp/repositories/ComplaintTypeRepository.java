package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, Integer> {
}
