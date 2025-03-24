package com.fleur.cinemate.search.entity;

import com.fleur.cinemate.__shared.model.IdEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "es_sync_dates")
public class ESSyncDate extends IdEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IndexName indexName;

    @Builder.Default
    @Column(nullable = false)
    private Instant lastSyncTime = Instant.now();
}
