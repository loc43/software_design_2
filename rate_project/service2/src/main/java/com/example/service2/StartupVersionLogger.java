package com.example.service2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupVersionLogger {

    private static final Logger log = LoggerFactory.getLogger(StartupVersionLogger.class);

    @Value("${info.app.version:unknown}")
    private String version;

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("version {}", version);
    }
}
