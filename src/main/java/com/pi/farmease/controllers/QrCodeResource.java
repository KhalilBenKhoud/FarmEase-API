 package com.pi.farmease.controllers;

 import com.google.zxing.NotFoundException;
 import com.google.zxing.WriterException;
import com.pi.farmease.entities.Garantor ;
 import com.pi.farmease.dto.responses.DecodedQrResponse ;
 import com.pi.farmease.services.QrCodeService ;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpHeaders;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.MediaType;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.MissingRequestValueException;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.RestController;
 import org.springframework.web.multipart.MultipartFile;

 import java.io.ByteArrayOutputStream;
 import java.io.IOException;

@RestController
public class QrCodeResource {

    @Autowired
    private QrCodeService qrCodeService;

    @PostMapping(path = "/api/qr/generate", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateQr(@RequestBody Garantor request) throws MissingRequestValueException, WriterException, IOException {
        if( request == null || request.getQrString() == null || request.getQrString().trim().equals("")) {
            throw new MissingRequestValueException("QR String is required");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        qrCodeService.generateQr(request.getQrString(), outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(qrCodeBytes.length);
        return new ResponseEntity<>(qrCodeBytes, headers, HttpStatus.OK);
    }

    @PostMapping(path = "/api/qr/decode")
    public ResponseEntity<DecodedQrResponse> decodeQr(@RequestParam("qrCode") MultipartFile qrCode) throws IOException, NotFoundException {
        String qrCodeString = qrCodeService.decodeQr(qrCode.getBytes());
        DecodedQrResponse response = new DecodedQrResponse(qrCodeString);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
