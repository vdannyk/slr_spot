package com.dkwasniak.slr_spot_backend.review.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Component
public class ReportFactory {

    private static final float FONT_SIZE = 14;
    private static final float FONT_SIZE_HEADER = 24;
    private static final float HEADER_LEADING = 35f;
    private static final float DEFAULT_LEADING = 15f;
    private static final float MARGIN_X = 40f;
    private static final float MARGIN_Y = 40f;
    private static final float IMAGE_WIDTH = 125;
    private static final float IMAGE_HEIGHT = 100;
    private float pageHeight;
    private float pageWidth;
    private PDFont FONT;
    private PDFont FONT_BOLD;

    public InputStreamResource createReport(ReportData reportData) {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PDDocument document = new PDDocument();
        ) {
            FONT = PDType0Font.load(document, getClass().getResourceAsStream("/LiberationSans-Regular.ttf"));
            FONT_BOLD = PDType0Font.load(document, getClass().getResourceAsStream("/LiberationSans-Bold.ttf"));
            PDPage page = new PDPage();
            PageData pageData;
            pageWidth = page.getMediaBox().getWidth();
            pageHeight = page.getMediaBox().getHeight();

            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile(
                    Objects.requireNonNull(getClass().getResource("/slrspot_logo.png")).getPath(), document
            );
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float pageCenterWidth = (pageWidth - IMAGE_WIDTH) / 2;
            float currentHeight = pageHeight - IMAGE_HEIGHT - MARGIN_Y;
            contentStream.drawImage(pdImage, pageCenterWidth, currentHeight, IMAGE_WIDTH, IMAGE_HEIGHT);


            float boxWidth = page.getMediaBox().getWidth() - 2 * MARGIN_X;
            float startX = page.getMediaBox().getLowerLeftX() + MARGIN_X;
            currentHeight -= 25;

            contentStream.beginText();
            // Header
            contentStream.setFont(FONT_BOLD, FONT_SIZE_HEADER);
            contentStream.setLeading(HEADER_LEADING);
            contentStream.newLineAtOffset(startX, currentHeight);
            contentStream.showText("Review Report");
            contentStream.newLine();

            currentHeight -= HEADER_LEADING;
            currentHeight -= MARGIN_Y;
            // Title
            contentStream.setLeading(DEFAULT_LEADING);
            if (isNull(reportData.getTitle())) {
                pageData = writeAttribute(contentStream, boxWidth, "Title: ", "-", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Title: ", reportData.getTitle(), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Maintainer
            if (isNull(reportData.getOwner())) {
                pageData = writeAttribute(contentStream, boxWidth, "Maintainer: ", "-", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Maintainer: ", reportData.getOwner(), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Research area
            if (isNull(reportData.getResearchArea())) {
                pageData = writeAttribute(contentStream, boxWidth, "Research area: ", "-", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Research area: ", reportData.getResearchArea(), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Description
            if (isNull(reportData.getDescription())) {
                pageData = writeAttribute(contentStream, boxWidth, "Description: ", "-", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Description: ", reportData.getDescription(), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Research questions
            currentHeight -= 15;
            contentStream.setFont(FONT_BOLD, FONT_SIZE);
            contentStream.showText("Research questions: ");
            contentStream.newLine();
            if (CollectionUtils.isEmpty(reportData.getResearchQuestions())) {
                currentHeight -= DEFAULT_LEADING;
                contentStream.setFont(FONT, FONT_SIZE);
                contentStream.showText("-");
                contentStream.newLine();
            } else {
                for (var question : reportData.getResearchQuestions()) {
                    pageData = writeAttribute(contentStream, boxWidth, " -- " + question, currentHeight, document, startX);
                    currentHeight = pageData.getCurrentHeight();
                    if (pageData.getContentStream() != null) {
                        contentStream = pageData.getContentStream();
                    }
                }
            }

            // Members
            currentHeight -= 15;
            contentStream.setFont(FONT_BOLD, FONT_SIZE);
            contentStream.showText("Members: ");
            contentStream.newLine();
            if (CollectionUtils.isEmpty(reportData.getMembers())) {
                currentHeight -= DEFAULT_LEADING;
                contentStream.setFont(FONT, FONT_SIZE);
                contentStream.showText("-");
                contentStream.newLine();
            } else {
                for (var member : reportData.getMembers()) {
                    pageData = writeAttribute(contentStream, boxWidth, " -- " + member, currentHeight, document, startX);
                    currentHeight = pageData.getCurrentHeight();
                    if (pageData.getContentStream() != null) {
                        contentStream = pageData.getContentStream();
                    }
                }
            }

            // Total studies imported
            if (isNull(reportData.getTotalStudiesImported())) {
                pageData = writeAttribute(contentStream, boxWidth, "Total studies imported: ", "0", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Total studies imported: ", String.valueOf(reportData.getTotalStudiesImported()), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Removed duplicates
            if (isNull(reportData.getRemovedDuplicates())) {
                pageData = writeAttribute(contentStream, boxWidth, "Removed duplicates: ", "0", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Removed duplicates: ", String.valueOf(reportData.getRemovedDuplicates()), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            // Selected studies
            if (isNull(reportData.getTotalStudiesImported())) {
                pageData = writeAttribute(contentStream, boxWidth, "Included studies: ", "0", currentHeight, document, startX);
            } else {
                pageData = writeAttribute(contentStream, boxWidth, "Included studies: ", String.valueOf(reportData.getSelectedStudies()), currentHeight, document, startX);
            }
            currentHeight = pageData.getCurrentHeight();
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
            }

            contentStream.endText();
            contentStream.close();

            document.save(outputStream);
            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException("Error while generating report");
        }
    }

    private PageData writeAttribute(PDPageContentStream contentStream, float boxWidth,
                                 String header, String text, float currentHeight, PDDocument document, float startX) throws IOException {
        contentStream.setFont(FONT_BOLD, FONT_SIZE);
        contentStream.showText(header);
        currentHeight -= DEFAULT_LEADING;
        var pageData = tryAddNewPage(currentHeight, contentStream, document, startX);
        if (pageData.getContentStream() != null) {
            contentStream = pageData.getContentStream();
            currentHeight = pageData.getCurrentHeight();
        }
        contentStream.newLine();
        return writeAttribute(contentStream, boxWidth, text, currentHeight, document, startX);
    }

    private PageData writeAttribute(PDPageContentStream contentStream, float boxWidth,
                                String text, float currentHeight, PDDocument document, float startX) throws IOException {
        List<String> lines = wrapText(text, boxWidth);
        for (var line : lines) {
            currentHeight -= DEFAULT_LEADING;
            var pageData = tryAddNewPage(currentHeight, contentStream, document, startX);
            if (pageData.getContentStream() != null) {
                contentStream = pageData.getContentStream();
                currentHeight = pageData.getCurrentHeight();
            }
            contentStream.setFont(FONT, FONT_SIZE);
            contentStream.showText(line);
            contentStream.newLine();
        }
        currentHeight -= DEFAULT_LEADING;
        var pageData = tryAddNewPage(currentHeight, contentStream, document, startX);
        if (pageData.getContentStream() != null) {
            contentStream = pageData.getContentStream();
            currentHeight = pageData.getCurrentHeight();
        }
        contentStream.newLine();
        return new PageData(currentHeight, contentStream);
    }

    private List<String> wrapText(String text, float width) throws IOException {
        List<String> lines = new ArrayList<>();

        int lastSpace = -1;
        while (text.length() > 0)
        {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0)
                spaceIndex = text.length();
            String subString = text.substring(0, spaceIndex);
            float size = FONT_SIZE * FONT.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0)
                    lastSpace = spaceIndex;
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }

    private PageData tryAddNewPage(float currentHeight, PDPageContentStream contentStream,
                                PDDocument document, float startX) throws IOException {
        if (currentHeight <= 0) {
            closePageContent(contentStream);
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            currentHeight = pageHeight - MARGIN_Y;
            var newContentStream = createNewPageContent(currentHeight, document, startX, newPage);
            currentHeight -= MARGIN_Y;
            return new PageData(currentHeight, newContentStream);
        }
        return new PageData(currentHeight);
    }

    private void closePageContent(PDPageContentStream contentStream) throws IOException {
        contentStream.endText();
        contentStream.close();
    }

    private PDPageContentStream createNewPageContent(float currentHeight, PDDocument document, float startX,
                                                     PDPage newPage) throws IOException {
        var contentStream = new PDPageContentStream(document, newPage);
        contentStream.beginText();
        contentStream.newLineAtOffset(startX, currentHeight);
        contentStream.newLine();
        contentStream.setLeading(DEFAULT_LEADING);
        return contentStream;
    }
}
