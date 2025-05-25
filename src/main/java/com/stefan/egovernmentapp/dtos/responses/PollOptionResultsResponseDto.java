package com.stefan.egovernmentapp.dtos.responses;

import com.stefan.egovernmentapp.models.PollOption;
import lombok.Builder;

@Builder
public record PollOptionResultsResponseDto(Integer optionId,
                                           String optionText,
                                           Integer optionCount) {
    public static PollOptionResultsResponseDto toDto(PollOption option, Integer optionCount) {
        return PollOptionResultsResponseDto.builder()
                .optionId(option.getId())
                .optionText(option.getOptionText())
                .optionCount(optionCount)
                .build();
    }
}
