package com.dkwasniak.slr_spot_backend.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FolderAssignDto {

    private List<Long> studiesId;
}
