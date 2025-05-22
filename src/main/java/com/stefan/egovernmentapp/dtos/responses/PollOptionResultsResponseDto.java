package com.stefan.egovernmentapp.dtos.responses;

import lombok.Builder;

@Builder
public record PollOptionResultsResponseDto(Integer optionId,
                                           String optionText,
                                           Integer optionCount) {
}
