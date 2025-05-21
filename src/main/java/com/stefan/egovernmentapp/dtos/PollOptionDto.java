package com.stefan.egovernmentapp.dtos;

import com.stefan.egovernmentapp.models.PollOption;
import lombok.Builder;

@Builder
public record PollOptionDto(Integer pollOptionId,
                            String optionText) {
    public static PollOptionDto toDto(PollOption pollOption) {
        return PollOptionDto.builder()
                .pollOptionId(pollOption.getId())
                .optionText(pollOption.getOptionText())
                .build();
    }
}
