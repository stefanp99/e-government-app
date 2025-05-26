package com.stefan.egovernmentapp.dtos.requests;

import lombok.Builder;

@Builder
public record PollRequestDto(String title,
                             String pollOptions) {
}
