package com.pi.farmease.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public class DecodedQrRequest {

    private MultipartFile qrCode;

    public MultipartFile getQrCode() {
        return qrCode;
    }

    public void setQrCode(MultipartFile qrCode) {
        this.qrCode = qrCode;
    }
}
