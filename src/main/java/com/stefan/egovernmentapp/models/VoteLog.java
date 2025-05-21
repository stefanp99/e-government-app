package com.stefan.egovernmentapp.models;

import com.stefan.egovernmentapp.models.embeddables.VoteLogId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "vote_logs")
public class VoteLog {
    @EmbeddedId
    private VoteLogId id;

    private LocalDateTime votedAt;
}
