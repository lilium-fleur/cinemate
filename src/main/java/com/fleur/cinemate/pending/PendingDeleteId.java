package com.fleur.cinemate.pending;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingDeleteId implements Serializable {
    private Long recordId;
    private Long entityName;
}
