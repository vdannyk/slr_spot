package com.dkwasniak.slr_spot_backend.imports;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ImportService {

    private final ImportRepository importRepository;

    public Page<Import> getImportsByReviewId(long reviewId, int page, int size) {
        PageRequest pageRq = PageRequest.of(page, size, Sort.by("date"));
        return importRepository.findByReview_Id(reviewId, pageRq);
    }

    public void deleteImportById(Long id) {
        importRepository.deleteById(id);
    }

}
