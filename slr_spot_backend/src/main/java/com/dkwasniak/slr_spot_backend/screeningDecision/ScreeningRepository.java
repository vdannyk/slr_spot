package com.dkwasniak.slr_spot_backend.screeningDecision;

import com.dkwasniak.slr_spot_backend.study.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<ScreeningDecision, ScreeningDecisionId> {

    Optional<ScreeningDecision> findByStudy_IdAndUser_IdAndStage(long studyId, long userId, Stage stage);
}
