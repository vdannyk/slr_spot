package com.dkwasniak.slr_spot_backend.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public void saveAll(List<Study> studies) {
        studyRepository.saveAll(studies);
    }
}
