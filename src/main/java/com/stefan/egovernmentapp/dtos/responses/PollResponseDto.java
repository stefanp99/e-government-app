package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.models.Poll;
import com.stefan.egovernmentapp.models.PollOption;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PollResponseDto(String title,
                              LocalDate creationDate,
                              List<String> pollOptionTexts) {
    public static PollResponseDto toDto(Poll poll) {
        return PollResponseDto.builder()
                .title(poll.getTitle())
                .creationDate(poll.getCreationDate())
                .pollOptionTexts(poll.getPollOptions().stream()
                        .map(PollOption::getOptionText)
                        .toList())
                .build();
    }
}
