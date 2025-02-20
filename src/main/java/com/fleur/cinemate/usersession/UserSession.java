package com.fleur.cinemate.usersession;

import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_sessions")
@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
