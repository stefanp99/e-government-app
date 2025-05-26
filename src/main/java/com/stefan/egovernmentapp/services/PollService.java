package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.requests.AddPollRequestDto;
import com.stefan.egovernmentapp.dtos.responses.PollResponseDto;
import com.stefan.egovernmentapp.models.Poll;
import com.stefan.egovernmentapp.models.PollOption;
import com.stefan.egovernmentapp.repositories.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor

@Service
public class PollService {
    private final PollRepository pollRepository;

    public ResponseEntity<String> addPoll(AddPollRequestDto addPollRequestDto) {
        Poll poll = Poll.builder()
                .title(addPollRequestDto.title())
                .creationDate(LocalDate.now())
                .active(false)
                .build();

        List<PollOption> pollOptions = Stream.of(addPollRequestDto.pollOptions().split(","))
                .map(String::trim)
                .map(optionText -> PollOption.builder()
                        .optionText(optionText)
                        .poll(poll)
                        .build())
                .toList();

        poll.setPollOptions(pollOptions);

        pollRepository.save(poll);
        return ResponseEntity.status(HttpStatus.CREATED).body("Poll created");
    }

    public ResponseEntity<String> startOrEndPoll(Integer pollId) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        if (optionalPoll.isPresent()) {
            Poll poll = optionalPoll.get();
            if (poll.getActive()) {
                poll.setActive(false);
                poll.setEndDate(LocalDate.now());
                pollRepository.save(poll);
                return ResponseEntity.status(HttpStatus.OK).body("Poll ended");
            } else {
                poll.setActive(true);
                pollRepository.save(poll);
                return ResponseEntity.status(HttpStatus.OK).body("Poll started");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found");
    }

    public ResponseEntity<List<PollResponseDto>> getPollsByActive(Boolean active) {
        return ResponseEntity.ok(pollRepository.findAll().stream()
                .filter(poll -> poll.getActive() == active)
                .map(PollResponseDto::toDto)
                .toList());
    }

    public ResponseEntity<List<PollResponseDto>> getAllPolls() {
        return ResponseEntity.ok(pollRepository.findAll().stream()
                .map(PollResponseDto::toDto)
                .toList());
    }
}
