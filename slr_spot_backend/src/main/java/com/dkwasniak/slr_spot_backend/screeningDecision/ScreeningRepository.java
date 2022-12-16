package com.dkwasniak.slr_spot_backend.screeningDecision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends JpaRepository<ScreeningDecision, ScreeningDecisionId> {


}
