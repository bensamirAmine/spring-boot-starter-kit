package com.bensamir.starter.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int page;
    private final int size;

    private PageResponse(List<T> content, long totalElements, int totalPages, int page, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
    }

    // Static factory method to create from Spring Page
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    // Static factory method to create with raw values
    public static <T> PageResponse<T> of(List<T> content, long totalElements, int totalPages, int page, int size) {
        return new PageResponse<>(content, totalElements, totalPages, page, size);
    }

    // Getters
    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}