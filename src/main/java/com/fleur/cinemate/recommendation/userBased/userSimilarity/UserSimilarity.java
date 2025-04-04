package com.fleur.cinemate.recommendation.userBased.userSimilarity;

import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "user_similarities",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user1", "user2"}
        ))
@IdClass(UserSimilarityId.class)
public class UserSimilarity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @Id
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(nullable = false)
    private Double similarity;
}
