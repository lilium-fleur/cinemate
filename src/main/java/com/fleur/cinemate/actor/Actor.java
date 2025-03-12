package com.fleur.cinemate.actor;

import com.fleur.cinemate.__shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "actors")
@Entity
public class Actor extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date born;

    private String portraitUrl;

}
