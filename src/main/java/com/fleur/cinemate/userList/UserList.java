package com.fleur.cinemate.userList;

import com.fleur.cinemate.__shared.model.IdEntity;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "user_lists",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"film_id", "type"}
        )
)
@Entity
public class UserList extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserListType type;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant addedAt;
}
