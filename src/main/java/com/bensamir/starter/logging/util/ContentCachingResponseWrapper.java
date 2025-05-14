package com.bensamir.starter.logging.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * HttpServletResponse wrapper that caches the response body.
 */
public class ContentCachingResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedContent;
    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private int statusCode = HttpServletResponse.SC_OK;

    public ContentCachingResponseWrapper(HttpServletResponse response) {
        super(response);
        this.cachedContent = new ByteArrayOutputStream();
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.statusCode = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        this.statusCode = sc;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);
        this.statusCode = sc;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new CachedServletOutputStream(cachedContent);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(cachedContent, getCharacterEncoding()));
        }
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
        super.flushBuffer();
    }

    /**
     * Gets the cached content as a byte array.
     */
    public byte[] getContentAsByteArray() {
        return cachedContent.toByteArray();
    }

    /**
     * Copies the cached content to the original response.
     */
    public void copyBodyToResponse() throws IOException {
        if (cachedContent.size() > 0) {
            byte[] content = cachedContent.toByteArray();
            getResponse().getOutputStream().write(content);
        }
    }

    /**
     * Gets the status code.
     */
    public int getStatus() {
        return statusCode;
    }

    /**
     * Cached ServletOutputStream implementation.
     */
    private static class CachedServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream byteStream;

        public CachedServletOutputStream(ByteArrayOutputStream byteStream) {
            this.byteStream = byteStream;
        }

        @Override
        public void write(int b) throws IOException {
            byteStream.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            byteStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byteStream.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
            throw new UnsupportedOperationException("setWriteListener is not supported");
        }
    }
}