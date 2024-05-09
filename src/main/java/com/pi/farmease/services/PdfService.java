package com.pi.farmease.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import com.pi.farmease.entities.Project;

@Service
public class PdfService {

    private static final float MARGIN = 30;
    private static final float LINE_SPACING = 15;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public byte[] generatePdf(Project project) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, page.getMediaBox().getHeight() - MARGIN); // Start at top with margin
                contentStream.showText("Project Details");
                contentStream.endText();
                contentStream.beginText();
                contentStream.showText("Project title");
                contentStream.endText();
                contentStream.beginText();
                contentStream.showText("Project category");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, -LINE_SPACING); // Move down after title

                // Use formatted string for each field with clear labels
                contentStream.showText(String.format("Title: %s", project.getTitle()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Description: %s", project.getDescription()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Net Income Last Year: %.2f", project.getNetIncomeLastYear()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Image URL: %s", project.getImageUrl()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Address: %s", project.getAddress()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Goal Amount: %.2f", project.getGoalAmount()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Deadline: %s", DATE_FORMAT.format(project.getDeadline())));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Equity Offered: %.2f %%", project.getEquityOffered()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Dividend Payout Ratio: %.2f %%", project.getDividendPayoutRatio()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Project Category: %s", project.getProjectCategory()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Project Status: %s", project.getProjectStatus()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);

                contentStream.showText(String.format("Created At: %s", DATE_FORMAT.format(project.getCreatedAt())));
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(MARGIN, page.getMediaBox().getHeight() - MARGIN); // Start at top with margin
                contentStream.showText("Project Details");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, -LINE_SPACING); // Move down after title

// Use formatted string for each field with clear labels
                contentStream.showText(String.format("Title: %s", project.getTitle()));
                contentStream.newLineAtOffset(0, -LINE_SPACING); // Move down with line spacing

                contentStream.showText(String.format("Description: %s", project.getDescription()));
                contentStream.newLineAtOffset(0, -LINE_SPACING);
            }

            document.save(outputStream);
        }

        return outputStream.toByteArray();
    }
}
