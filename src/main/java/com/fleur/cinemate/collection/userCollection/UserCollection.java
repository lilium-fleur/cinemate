package com.fleur.cinemate.collection.userCollection;

import com.fleur.cinemate.collection.Collection;
import com.fleur.cinemate.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_collections",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id, collection_id"})
)
@IdClass(UserCollectionId.class)
public class UserCollection {

    @Id
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "collection_id", nullable = false)
    @ManyToOne
    private Collection collection;

    @Column(nullable = false)
    @CreationTimestamp
    private Instant addedAt;
}
