package com.fleur.cinemate.token.shared;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "tokens")
@Entity
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    @CreatedDate
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isRevoked = Boolean.FALSE;

}
