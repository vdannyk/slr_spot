package com.dkwasniak.slr_spot_backend.screeningDecision;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;


    public void updateDecision(ScreeningDecision screeningDecision, Decision decision) {
        screeningDecision.setDecision(decision);
        screeningRepository.save(screeningDecision);
    }
}
