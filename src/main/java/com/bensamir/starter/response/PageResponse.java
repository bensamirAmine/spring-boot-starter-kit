package com.bensamir.starter.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

/**
 * Standard response wrapper for paginated data.
 * <p>
 * This class provides a consistent structure for paginated responses, including:
 * <ul>
 *   <li>Content for the current page</li>
 *   <li>Pagination information (total elements, pages, etc.)</li>
 * </ul>
 *
 * @param <T> The type of content items
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int page;
    private final int size;
    private final boolean first;
    private final boolean last;

    private PageResponse(List<T> content, long totalElements, int totalPages,
                         int page, int size, boolean first, boolean last) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
        this.first = first;
        this.last = last;
    }

    /**
     * Creates a page response from a Spring Data Page.
     *
     * @param page The Spring Data Page
     * @param <T> The type of content items
     * @return A page response
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }

    /**
     * Creates a page response from individual components.
     *
     * @param content The page content
     * @param totalElements Total number of elements
     * @param totalPages Total number of pages
     * @param page Current page number (0-based)
     * @param size Page size
     * @param <T> The type of content items
     * @return A page response
     */
    public static <T> PageResponse<T> of(List<T> content, long totalElements,
                                         int totalPages, int page, int size) {
        boolean first = page == 0;
        boolean last = page >= totalPages - 1;

        return new PageResponse<>(content, totalElements, totalPages, page, size, first, last);
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

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    /**
     * Checks if the page has any content.
     *
     * @return true if the page has content, false otherwise
     */
    public boolean hasContent() {
        return content != null && !content.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageResponse<?> that = (PageResponse<?>) o;
        return totalElements == that.totalElements &&
                totalPages == that.totalPages &&
                page == that.page &&
                size == that.size &&
                first == that.first &&
                last == that.last &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements, totalPages, page, size, first, last);
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "content.size=" + (content != null ? content.size() : 0) +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", page=" + page +
                ", size=" + size +
                ", first=" + first +
                ", last=" + last +
                '}';
    }
}