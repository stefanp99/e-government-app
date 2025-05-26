package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.dtos.PollOptionDto;
import com.stefan.egovernmentapp.models.Poll;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PollResponseDto(
        Integer id,
        String title,
        LocalDate creationDate,
        Boolean active,
        List<PollOptionDto> pollOptions) {
    public static PollResponseDto toDto(Poll poll) {
        return PollResponseDto.builder()
                .id(poll.getId())
                .title(poll.getTitle())
                .creationDate(poll.getCreationDate())
                .active(poll.getActive())
                .pollOptions(poll.getPollOptions().stream()
                        .map(PollOptionDto::toDto)
                        .toList())
                .build();
    }
}
