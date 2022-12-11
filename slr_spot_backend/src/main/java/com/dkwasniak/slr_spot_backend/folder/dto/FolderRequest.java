package com.dkwasniak.slr_spot_backend.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FolderRequest {

    private String name;
    private Long parentId;
    private Long reviewId;
}
