package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Integer> {
}
