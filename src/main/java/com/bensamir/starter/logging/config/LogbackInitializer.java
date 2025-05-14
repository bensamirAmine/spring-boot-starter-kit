package com.bensamir.starter.logging.config;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

public class LogbackInitializer implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        try {
            // Load custom Logback configuration
            ClassPathResource resource = new ClassPathResource("com/bensamir/starter/logging/logback-starter.xml");
            InputStream inputStream = resource.getInputStream();

            // Configure Logback
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            configurator.doConfigure(inputStream);

        } catch (IOException | JoranException e) {
            System.err.println("Failed to load custom Logback configuration: " + e.getMessage());
        }
    }
}