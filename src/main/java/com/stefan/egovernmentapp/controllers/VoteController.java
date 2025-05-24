package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.AddVoteRequestDto;
import com.stefan.egovernmentapp.dtos.responses.PollResultsResponseDto;
import com.stefan.egovernmentapp.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor

@RestController
@RequestMapping("votes")
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<String> castVote(@RequestHeader("Authorization") String token,
                                           @RequestBody AddVoteRequestDto addVoteRequestDto) {
        return voteService.castVote(token, addVoteRequestDto);
    }

    @GetMapping("results/{id}")
    public ResponseEntity<PollResultsResponseDto> getPollResults(@PathVariable Integer id) {
        return voteService.getPollResults(id);
    }

    @GetMapping("resident-has-voted/{pollId}")
    public ResponseEntity<Boolean> hasResidentVoted(@RequestHeader("Authorization") String token,
                                                    @PathVariable Integer pollId) {
        return voteService.hasResidentVoted(token, pollId);
    }
}
