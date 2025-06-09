package com.fleur.cinemate.pendingDelete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingDeleteRepository extends JpaRepository<PendingDelete, PendingDeleteId> {
}
