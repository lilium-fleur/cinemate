package com.fleur.cinemate.user.session;

import com.fleur.cinemate.__shared.model.BaseEntity;
import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_sessions")
@Entity
public class UserSession extends BaseEntity {

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "device_info", nullable = false)
    private String deviceInfo;

    @Column(nullable = false)
    private String fingerprint;

    @Column(name = "last_activity", nullable = false)
    private Instant lastActivity;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
