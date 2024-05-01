package com.pi.farmease.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pi.farmease.dao.GarantorRepository;
import com.pi.farmease.dao.creditRepository;
import com.pi.farmease.entities.Credit;
import com.pi.farmease.entities.Garantor;
import com.pi.farmease.entities.User;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GarantorService implements IGarantorService {

    private  GarantorRepository garantroRepo ;
    private creditRepository  creditRepo ;
    private final UserService userService ;
    private CreditService crService ;
    public List<Garantor> getGarantor()
    {
        return garantroRepo.findAll() ;
    }

    public Optional<Garantor> getGarantorById(long idGarantor)
    {
        return garantroRepo.findById(idGarantor) ;
    }

    public Garantor addGarantor(Garantor garantor, Long creditId, Principal connected) {
        // Récupérer l'utilisateur connecté
        User connectedUser = userService.getCurrentUser(connected);

        // Définir l'utilisateur connecté pour le garant
        garantor.setUser(connectedUser);

        // Vérifier si l'ID du crédit est null ou non
        if (creditId == null) {
            throw new IllegalArgumentException("L'ID du crédit ne peut pas être null.");
        }

        // Construire le Garantor à partir des informations fournies
        Garantor garantor1 = Garantor.builder()
                .user(connectedUser)
                .nameGarantor(garantor.getNameGarantor())
                .secondnameGarantor(garantor.getSecondnameGarantor())
                .salaryGarantor(garantor.getSalaryGarantor())
                .workGarantor(garantor.getWorkGarantor())
                .qrString(garantor.getQrString())
                .build();

        // Récupérer le crédit associé à partir de l'ID
        Credit credit = new Credit();
        credit.setIdCredit(creditId);
        garantor1.setCredit(credit);

        // Sauvegarder le Garantor dans la base de données
        Garantor savedGarantor = garantroRepo.save(garantor1);

        return savedGarantor;
    }

    public void updateGarantor(Garantor garantor, Long idGarantor) {
        garantroRepo.save(garantor);
    }

    public boolean existById(Long idGarantor)
    {
        return garantroRepo.existsById(idGarantor);
    }

    public void DeleteGarantor(Long idGarantor) {
        garantroRepo.deleteById(idGarantor);
    }

    // Méthode pour sauvegarder le document PDF associé au dernier garant
    public Optional<Garantor> findLastGarantor() {
        return garantroRepo.findFirstByOrderByIdGarantorDesc();
    }

    // Méthode pour sauvegarder le document PDF associé au dernier garant
    public void savePdfDocumentForLastGarantor(MultipartFile file) throws IOException {
        // Récupération du dernier garant à partir du dépôt
        Optional<Garantor> optionalGarantor = findLastGarantor();
        // Vérification si un garant est trouvé
        if (optionalGarantor.isPresent()) {
            Garantor garantor = optionalGarantor.get();
            // Mise à jour du champ pdfDocument avec le contenu du fichier PDF
            garantor.setPdfDocument(file.getBytes());
            // Sauvegarde de l'entité Garantor mise à jour dans la base de données
            garantroRepo.save(garantor);
        } else {
            // Si aucun garant n'est trouvé, une exception est levée
            throw new IllegalArgumentException("No garantor found to associate the PDF document with.");
        }
    }

    // Méthode pour récupérer le document PDF associé au dernier garant
    public byte[] getPdfDocumentForLastGarantor() {
        // Récupération du dernier garant à partir du dépôt
        Optional<Garantor> optionalGarantor = findLastGarantor();
        // Vérification si un garant est trouvé
        if (optionalGarantor.isPresent()) {
            Garantor garantor = optionalGarantor.get();
            // Renvoie le contenu du champ pdfDocument de ce garant
            return garantor.getPdfDocument();
        } else {
            // Si aucun garant n'est trouvé, une exception est levée
            throw new IllegalArgumentException("No garantor found to retrieve the PDF document from.");
        }
    }


    public byte[] generateQRCodeForAllPDFs() throws IOException, WriterException {
        Iterable<Garantor> allGarantors = garantroRepo.findAll();

        ByteArrayOutputStream qrCodeOutputStream = new ByteArrayOutputStream();

        for (Garantor garantor : allGarantors) {
            if (garantor.getPdfDocument() != null) {
                PDDocument document = PDDocument.load(new ByteArrayInputStream(garantor.getPdfDocument()));
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos);
                byte[] imageBytes = baos.toByteArray();

                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(new String(imageBytes), BarcodeFormat.QR_CODE, 200, 200);
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrCodeOutputStream);
            }
        }

        return qrCodeOutputStream.toByteArray();
    }
}

