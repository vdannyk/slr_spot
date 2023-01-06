package com.dkwasniak.slr_spot_backend.study;

import lombok.Getter;

@Getter
public enum SortProperty {
    TITLE("title"),
    AUTHORS("authors"),
    YEAR("publicationYear");

    private final String name;

    SortProperty(String name) {
        this.name = name;
    }
}
