package com.fleur.cinemate.pending;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "pending_delete",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"record_id", "entity_name"}
        ))
@IdClass(PendingDeleteId.class)
public class PendingDelete {

    @Id
    @Column(nullable = false)
    private Long recordId;

    @Id
    @Column(nullable = false)
    private String entityName;
}
