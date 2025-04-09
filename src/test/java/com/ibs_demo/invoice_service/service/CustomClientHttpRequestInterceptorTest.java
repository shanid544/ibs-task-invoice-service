package com.ibs_demo.invoice_service.service;

import com.ibs_demo.invoice_service.config.resttemplate.CustomClientHttpRequestInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomClientHttpRequestInterceptorTest {

    private CustomClientHttpRequestInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new CustomClientHttpRequestInterceptor();
    }

    @Test
    void testInterceptLogsAndExecutesRequest() throws IOException {
        HttpRequest request = mock(HttpRequest.class);
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse expectedResponse = mock(ClientHttpResponse.class);

        byte[] body = "test-body".getBytes();

        when(request.getURI()).thenReturn(URI.create("http://localhost/test"));
        when(execution.execute(request, body)).thenReturn(expectedResponse);

        ClientHttpResponse actualResponse = interceptor.intercept(request, body, execution);

        assertEquals(expectedResponse, actualResponse);
        verify(request, times(1)).getURI();
        verify(execution, times(1)).execute(request, body);
    }
}
