package com.dkwasniak.slr_spot_backend.study;

import com.dkwasniak.slr_spot_backend.util.EndpointConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointConstants.API_PATH + "/studies")
public class StudyController {

    private final StudyFacade studyFacade;
}