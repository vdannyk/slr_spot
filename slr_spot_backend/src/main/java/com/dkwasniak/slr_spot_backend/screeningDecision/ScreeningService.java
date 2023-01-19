package com.dkwasniak.slr_spot_backend.screeningDecision;

import com.dkwasniak.slr_spot_backend.study.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;


    @Transactional
    public void updateDecision(ScreeningDecision screeningDecision, Decision decision) {
        screeningDecision.setDecision(decision);
        screeningRepository.save(screeningDecision);
    }

    public ScreeningDecision getScreeningDecisionByStudyIdAndUserId(Long studyId, Long userId, Stage stage) {
        return screeningRepository.findByStudy_IdAndUser_IdAndStage(studyId, userId, stage).orElseThrow();
    }
}
