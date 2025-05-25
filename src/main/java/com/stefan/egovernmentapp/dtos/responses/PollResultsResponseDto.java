package com.stefan.egovernmentapp.dtos.responses;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PollResultsResponseDto(Integer id,
                                     String title,
                                     LocalDate creationDate,
                                     List<PollOptionResultsResponseDto> pollOptionResults) {
}
