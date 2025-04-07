package com.ibs_demo.invoice_service.listener;

import com.ibs_demo.invoice_service.config.NotificationMailConfig;
import com.ibs_demo.invoice_service.config.ServiceUrlConfig;
import com.ibs_demo.invoice_service.entity.Invoice;
import com.ibs_demo.invoice_service.event.InvoiceCreatedEvent;
import com.ibs_demo.invoice_service.request.InvoiceNotificationPayload;
import com.ibs_demo.invoice_service.utils.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class InvoiceEventListener {

    private final WebClient webClient;
    private final NotificationMailConfig mailConfig;

    public InvoiceEventListener(WebClient.Builder webClientBuilder,
                                ServiceUrlConfig config,
                                NotificationMailConfig mailConfig) {
        this.webClient = webClientBuilder.baseUrl(config.getNotificationServiceUrl()).build();
        this.mailConfig = mailConfig;
    }



    @Async
    @EventListener
    public void handleInvoiceCreatedEvent(InvoiceCreatedEvent event) {
        Invoice invoice = event.getInvoice();

        InvoiceNotificationPayload payload = InvoiceMapper.toNotificationPayload(invoice);

        String mailVersion = mailConfig.getVersion();
        log.info("Sending invoice notification using mail version: {}", mailVersion);


        webClient.post()
                .uri("/api/notify")
                .header("X-Mail-Version", mailVersion)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> log.error("Notification failed: ", error))
                .subscribe();

        log.info("Invoice notification sent for invoice {}", invoice.getBillingId());
    }
}
