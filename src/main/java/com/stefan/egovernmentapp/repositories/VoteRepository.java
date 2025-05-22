package com.stefan.egovernmentapp.repositories;

import com.stefan.egovernmentapp.models.Poll;
import com.stefan.egovernmentapp.models.PollOption;
import com.stefan.egovernmentapp.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    List<Vote> findByPollAndPollOption(Poll poll, PollOption pollOption);
}
