package com.ibs_demo.invoice_service.config.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;

import java.io.IOException;

@Slf4j
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution
    ) throws IOException {

        log.info("Making request to URI: {}", request.getURI());
        return execution.execute(request, body);
    }
}
