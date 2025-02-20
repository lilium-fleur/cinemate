package com.fleur.cinemate.token.access;

import com.fleur.cinemate.token.shared.TokenEntity;
import com.fleur.cinemate.usersession.UserSession;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@DiscriminatorValue("ACCESS")
@Entity
public class AccessToken extends TokenEntity {

    @ManyToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}
