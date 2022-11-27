package com.dkwasniak.slr_spot_backend.criterion;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriterionService {

    private final CriterionRepository criterionRepository;
    private final CriterionTypeRepository criterionTypeRepository;

    public CriterionType getTypeByName(String typeName) {
        return criterionTypeRepository.findByName(typeName).orElseThrow();
    }

    public boolean checkIfExistByNameAndTypeName(String criterionName, String typeName) {
        return criterionRepository.existsByNameAndType_Name(criterionName, typeName);
    }

    public Criterion getByNameAndType(String criterionName, CriterionType type) {
        return criterionRepository.findByNameAndType(criterionName, type).orElseThrow();
    }
}
