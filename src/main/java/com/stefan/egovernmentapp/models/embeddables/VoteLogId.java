package com.stefan.egovernmentapp.models.embeddables;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
public class VoteLogId implements Serializable {
    private Integer pollId;
    private String residentId;
}
