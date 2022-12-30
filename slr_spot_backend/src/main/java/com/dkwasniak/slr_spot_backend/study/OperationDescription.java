package com.dkwasniak.slr_spot_backend.study;

import lombok.Getter;

@Getter
public enum OperationDescription {

    IMPORT("Import performed"),
    MARK_DUPLICATE("Marked as duplicate"),
    RESTORE_TO_SCREENING("Restored to screening"),
    ADD_TAG("Tag %s added"),
    REMOVE_TAG("Tag %s removed"),
    ADD_COMMENT("Comment added by %s"),
    VOTE("Vote cast by %s"),
    CHANGE_VOTE("Vote changed by %s"),
    CHANGE_STATUS("Status changed to %s"),
    LOAD_FULLTEXT("Full-text loaded"),
    REMOVE_FULLTEXT("Full-text removed"),
    DATA_EXTRACTION("Data extraction performed"),
    EXPORT_TO_FILE("Export performed");

    private final String description;

    OperationDescription(String description) {
        this.description = description;
    }
}
