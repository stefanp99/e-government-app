package com.stefan.egovernmentapp.services;

import com.stefan.egovernmentapp.dtos.requests.PollRequestDto;
import com.stefan.egovernmentapp.dtos.responses.PollResponseDto;
import com.stefan.egovernmentapp.models.Poll;
import com.stefan.egovernmentapp.models.PollOption;
import com.stefan.egovernmentapp.repositories.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor

@Service
public class PollService {
    private final PollRepository pollRepository;

    public ResponseEntity<String> addPoll(PollRequestDto pollRequestDto) {
        Poll poll = Poll.builder()
                .title(pollRequestDto.title())
                .creationDate(LocalDate.now())
                .active(false)
                .build();

        List<PollOption> pollOptions = Stream.of(pollRequestDto.pollOptions().split(","))
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
                .sorted(Comparator.comparing(PollResponseDto::creationDate).reversed())
                .toList());
    }

    public ResponseEntity<String> editPoll(Integer pollId, PollRequestDto pollRequestDto) {
        Optional<Poll> optionalPoll = pollRepository.findById(pollId);
        return optionalPoll.map(poll -> {
                    poll.setTitle(pollRequestDto.title());

                    poll.getPollOptions().clear();

                    List<PollOption> pollOptionList = Stream.of(pollRequestDto.pollOptions().split(","))
                            .map(String::trim)
                            .map(optionText -> PollOption.builder()
                                    .optionText(optionText)
                                    .poll(poll)
                                    .build())
                            .toList();

                    poll.getPollOptions().addAll(pollOptionList);
                    pollRepository.save(poll);
                    return ResponseEntity.status(HttpStatus.OK).body("Poll edited");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Poll not found"));
    }
}
