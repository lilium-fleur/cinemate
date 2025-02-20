package com.fleur.cinemate.token.refresh;


import com.fleur.cinemate.token.shared.TokenEntity;
import com.fleur.cinemate.usersession.UserSession;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@DiscriminatorValue("REFRESH")
@Entity
public class RefreshToken  extends TokenEntity {

    @OneToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}
