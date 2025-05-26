package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.PollRequestDto;
import com.stefan.egovernmentapp.dtos.responses.PollResponseDto;
import com.stefan.egovernmentapp.services.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("polls")
public class PollController {
    private final PollService pollService;

    @PostMapping
    public ResponseEntity<String> addPoll(@RequestBody PollRequestDto pollRequestDto) {
        return pollService.addPoll(pollRequestDto);
    }

    @PatchMapping("{pollId}")
    public ResponseEntity<String> startOrEndPoll(@PathVariable("pollId") Integer pollId) {
        return pollService.startOrEndPoll(pollId);
    }

    @GetMapping("{active}")
    public ResponseEntity<List<PollResponseDto>> getPollsByActive(@PathVariable("active") Boolean active) {
        return pollService.getPollsByActive(active);
    }

    @GetMapping
    public ResponseEntity<List<PollResponseDto>> getAllPolls() {
        return pollService.getAllPolls();
    }

    @PutMapping("{pollId}")
    public ResponseEntity<String> editPoll(@PathVariable("pollId") Integer pollId,
                                           @RequestBody PollRequestDto pollRequestDto) {
        return pollService.editPoll(pollId, pollRequestDto);
    }
}
