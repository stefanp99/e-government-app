package com.stefan.egovernmentapp.dtos.requests;

import lombok.Builder;

@Builder
public record AddVoteRequestDto(Integer pollId, Integer pollOptionId) {
}
