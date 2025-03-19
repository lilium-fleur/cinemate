package com.fleur.cinemate.auth.token.shared;

import com.fleur.cinemate.__shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "tokens")
@Entity
public class TokenEntity extends BaseEntity {

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isRevoked = Boolean.FALSE;

}
