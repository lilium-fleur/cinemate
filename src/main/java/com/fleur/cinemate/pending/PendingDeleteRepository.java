package com.fleur.cinemate.pending;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingDeleteRepository extends JpaRepository<PendingDelete, Long> {
}
