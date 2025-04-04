package com.fleur.cinemate.core.rating;


import com.fleur.cinemate.__shared.model.BaseEntity;
import com.fleur.cinemate.core.film.Film;
import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "ratings",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "film_id"})
)
public class Rating extends BaseEntity {

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @JoinColumn(name = "film_id", nullable = false)
    @ManyToOne
    private Film film;

    @Column(nullable = false)
    private Double rating;

}
