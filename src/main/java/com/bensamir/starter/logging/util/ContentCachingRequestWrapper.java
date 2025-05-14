package com.bensamir.starter.logging.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpServletRequest wrapper that caches the request body.
 */
public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream cachedContent;
    private ServletInputStream inputStream;
    private BufferedReader reader;

    public ContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        this.cachedContent = new ByteArrayOutputStream();
        try {
            // Cache request body
            ServletInputStream input = request.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                cachedContent.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // Ignore errors
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = new CachedServletInputStream(cachedContent.toByteArray());
        }
        return inputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return reader;
    }

    public byte[] getContentAsByteArray() {
        return cachedContent.toByteArray();
    }

    /**
     * Cached ServletInputStream implementation.
     */
    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream buffer;

        public CachedServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return buffer.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("setReadListener is not supported");
        }
    }
}