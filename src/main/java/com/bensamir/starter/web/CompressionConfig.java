package com.bensamir.starter.web;

import com.bensamir.starter.properties.StarterKitProperties;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.util.unit.DataSize;

/**
 * Configuration for HTTP response compression.
 * <p>
 * This class configures response compression with options for:
 * <ul>
 *   <li>Enabling/disabling compression</li>
 *   <li>Configuring minimum response size for compression</li>
 *   <li>Setting which MIME types should be compressed</li>
 * </ul>
 */
public class CompressionConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    private final StarterKitProperties properties;

    /**
     * Creates a new CompressionConfig.
     *
     * @param properties The starter kit properties
     */
    public CompressionConfig(StarterKitProperties properties) {
        this.properties = properties;
    }

    /**
     * Customizes the web server factory to configure compression.
     *
     * @param factory The web server factory to customize
     */
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        StarterKitProperties.WebConfigProperties.CompressionProperties compressionProps =
                properties.getWebConfig().getCompression();

        if (compressionProps.isEnabled()) {
            Compression compression = new Compression();
            compression.setEnabled(true);
            compression.setMinResponseSize(compressionProps.getMinResponseSize());
            compression.setMimeTypes(compressionProps.getMimeTypes());
            factory.setCompression(compression);
        }

        // Enable default servlet registration
        factory.setRegisterDefaultServlet(true);
    }
}