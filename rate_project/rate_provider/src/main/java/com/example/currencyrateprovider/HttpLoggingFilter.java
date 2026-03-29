package com.example.currencyrateprovider;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class HttpLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);

    private final MeterRegistry meterRegistry;

    public HttpLoggingFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("request: {} {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("response: status={}", response.getStatus());
        int status = response.getStatus();
        String client = request.getHeader("X-Client-Id");
        if (client == null) {
            client = "unknown";
        }
        if (status >= 500) {
            meterRegistry.counter("http.server.5xx", "client", client).increment();
        }
    }
}
