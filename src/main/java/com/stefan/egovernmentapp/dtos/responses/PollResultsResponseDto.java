package com.stefan.egovernmentapp.dtos.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record PollResultsResponseDto(Integer id,
                                     String title,
                                     List<PollOptionResultsResponseDto> pollOptionResults) {
}
