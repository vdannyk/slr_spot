package com.dkwasniak.slr_spot_backend.review.report;

import lombok.Data;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

@Data
public class PageData {

    private float currentHeight;
    private PDPageContentStream contentStream;

    public PageData(float currentHeight, PDPageContentStream contentStream) {
        this.currentHeight = currentHeight;
        this.contentStream = contentStream;
    }

    public PageData(float currentHeight) {
        this.currentHeight = currentHeight;
    }
}
