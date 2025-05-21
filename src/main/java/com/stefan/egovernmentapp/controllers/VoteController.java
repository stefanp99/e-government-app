package com.stefan.egovernmentapp.controllers;

import com.stefan.egovernmentapp.dtos.requests.AddVoteRequestDto;
import com.stefan.egovernmentapp.services.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
