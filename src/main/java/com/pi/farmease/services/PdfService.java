package com.pi.farmease.services;

import com.pi.farmease.entities.Project;
import com.pi.farmease.entities.enumerations.ProjectCategory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PdfService {

    public byte[] generatePdf(Project project) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Project Details");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 670);
                contentStream.showText("Title: " + project.getTitle());
                contentStream.newLine();
                contentStream.showText("Description: " + project.getDescription());
                contentStream.newLine();
                contentStream.showText("Net Income Last Year: " + project.getNetIncomeLastYear());
                contentStream.newLine();
                contentStream.showText("Image URL: " + project.getImageUrl());
                contentStream.newLine();
                contentStream.showText("Address: " + project.getAddress());
                contentStream.newLine();
                contentStream.showText("Goal Amount: " + project.getGoalAmount());
                contentStream.newLine();
                contentStream.showText("Deadline: " + formatDate(project.getDeadline()));
                contentStream.newLine();
                contentStream.showText("Equity Offered: " + project.getEquityOffered());
                contentStream.newLine();
                contentStream.showText("Dividend Payout Ratio: " + project.getDividendPayoutRatio());
                contentStream.newLine();
                contentStream.showText("Project Category: " + project.getProjectCategory());
                contentStream.newLine();
                contentStream.showText("Project Status: " + project.getProjectStatus());
                contentStream.newLine();
                contentStream.showText("Created At: " + formatDate(project.getCreatedAt()));
                contentStream.endText();
            }

            document.save(outputStream);
        }

        return outputStream.toByteArray();
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
