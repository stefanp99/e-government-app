package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.requests.AddVoteRequestDto;
import com.stefan.egovernmentapp.dtos.responses.PollOptionResultsResponseDto;
import com.stefan.egovernmentapp.dtos.responses.PollResultsResponseDto;
import com.stefan.egovernmentapp.models.Poll;
import com.stefan.egovernmentapp.models.PollOption;
import com.stefan.egovernmentapp.models.Resident;
import com.stefan.egovernmentapp.models.Vote;
import com.stefan.egovernmentapp.models.VoteLog;
import com.stefan.egovernmentapp.models.embeddables.VoteLogId;
import com.stefan.egovernmentapp.repositories.PollOptionRepository;
import com.stefan.egovernmentapp.repositories.PollRepository;
import com.stefan.egovernmentapp.repositories.VoteLogRepository;
import com.stefan.egovernmentapp.repositories.VoteRepository;
import com.stefan.egovernmentapp.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoteLogRepository voteLogRepository;
    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> castVote(String token, AddVoteRequestDto addVoteRequestDto) {
        Optional<Poll> optionalPoll = pollRepository.findById(addVoteRequestDto.pollId());
        Optional<PollOption> optionalPollOption = pollOptionRepository.findById(addVoteRequestDto.pollOptionId());
        if (optionalPoll.isEmpty() || optionalPollOption.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll or option does not exist");
        }
        Poll poll = optionalPoll.get();
        PollOption pollOption = optionalPollOption.get();

        if (!poll.getActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Poll is not active");
        }

        Optional<Resident> optionalResident = jwtUtil.findResidentByToken(token);
        if (optionalResident.isPresent()) {
            Resident resident = optionalResident.get();
            if (hasResidentVoted(resident, poll)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Resident already voted");
            }
            Vote vote = Vote.builder()
                    .poll(poll)
                    .pollOption(pollOption)
                    .build();
            voteRepository.save(vote);
            VoteLog voteLog = VoteLog.builder()
                    .id(VoteLogId.builder()
                            .pollId(poll.getId())
                            .residentId(resident.getId())
                            .build())
                    .poll(poll)
                    .resident(resident)
                    .votedAt(LocalDateTime.now())
                    .build();
            voteLogRepository.save(voteLog);
            return ResponseEntity.status(HttpStatus.CREATED).body("Vote added successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
    }

    public ResponseEntity<PollResultsResponseDto> getPollResults(Integer pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isPresent()) {
            Poll poll = optionalPoll.get();
            PollResultsResponseDto pollResultsResponseDto = getPollResults(poll);
            if (pollResultsResponseDto != null) {
                return ResponseEntity.ok(pollResultsResponseDto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<Boolean> hasResidentVoted(String token, Integer pollId) {
        Optional<Resident> optionalResident = jwtUtil.findResidentByToken(token);
        if (optionalResident.isPresent()) {
            Resident resident = optionalResident.get();
            Optional<Poll> optionalPoll = pollRepository.findById(pollId);
            if (optionalPoll.isPresent()) {
                Poll poll = optionalPoll.get();
                if (hasResidentVoted(resident, poll)) {
                    return ResponseEntity.status(HttpStatus.OK).body(true);
                }
                return ResponseEntity.status(HttpStatus.OK).body(false);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    public ResponseEntity<List<PollResultsResponseDto>> getAllPollResults() {
        List<Poll> pollList = pollRepository.findAll();
        List<PollResultsResponseDto> pollResultsResponseDtoList = new ArrayList<>();
        pollList.forEach(poll -> {
            PollResultsResponseDto pollResults = getPollResults(poll);
            if (pollResults != null)
                pollResultsResponseDtoList.add(pollResults);
        });
        return ResponseEntity.ok(pollResultsResponseDtoList.stream()
                .sorted(Comparator.comparing(PollResultsResponseDto::endDate).reversed())
                .toList());
    }

    private Boolean hasResidentVoted(Resident resident, Poll poll) {
        Optional<VoteLog> optionalVoteLog = voteLogRepository.findByResidentAndPoll(resident, poll);
        return optionalVoteLog.isPresent();
    }

    private PollResultsResponseDto getPollResults(Poll poll) {
        List<PollOptionResultsResponseDto> pollOptionResultsResponseDtoList = new ArrayList<>();
        if (!poll.getActive() && poll.getEndDate() != null) {
            List<PollOption> pollOptions = poll.getPollOptions();
            pollOptions.forEach(option -> pollOptionResultsResponseDtoList.add(
                    PollOptionResultsResponseDto.toDto(option, option.getVotes().size())));

            return PollResultsResponseDto.builder()
                    .id(poll.getId())
                    .title(poll.getTitle())
                    .endDate(poll.getEndDate())
                    .pollOptionResults(pollOptionResultsResponseDtoList)
                    .build();
        }
        return null;
    }
}
