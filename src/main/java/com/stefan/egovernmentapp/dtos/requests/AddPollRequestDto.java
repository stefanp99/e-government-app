package com.stefan.egovernmentapp.dtos.requests;

import lombok.Builder;

@Builder
public record AddPollRequestDto(String title,
                                String pollOptions) {
}
