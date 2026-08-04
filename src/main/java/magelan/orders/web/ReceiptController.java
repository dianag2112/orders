package magelan.orders.web;

import lombok.RequiredArgsConstructor;
import magelan.orders.order.service.ReceiptPdfService;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptPdfService receiptPdfService;

    @GetMapping(
            value = "/{orderId}/receipt",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> getReceipt(
            @PathVariable UUID orderId,
            @RequestParam(defaultValue = "bg") String lang
    ) {
        String language =
                "en".equalsIgnoreCase(lang)
                        ? "en"
                        : "bg";

        byte[] pdf = receiptPdfService.generateReceipt(
                orderId,
                language
        );

        String filename =
                "magelan-receipt-"
                        + orderId
                        + "-"
                        + language
                        + ".pdf";

        ContentDisposition contentDisposition =
                ContentDisposition
                        .inline()
                        .filename(filename)
                        .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition.toString()
                )
                .cacheControl(CacheControl.noStore())
                .body(pdf);
    }
}