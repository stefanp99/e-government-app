package com.stefan.egovernmentapp.models.embeddables;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class VoteLogId implements Serializable {
    private Integer pollId;
    private Integer residentId;
}
