package com.fleur.cinemate.userCollection.userList;

import com.fleur.cinemate.__shared.model.IdEntity;
import com.fleur.cinemate.film.Film;
import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.Date;

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
    @Builder.Default
    private Instant addedAt = Instant.now();
}
