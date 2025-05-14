package com.bensamir.starter.response;

import java.util.HashMap;
import java.util.Map;

public class MetaData {
    private final Map<String, Object> data;

    private MetaData(Map<String, Object> data) {
        this.data = data;
    }

    // Static factory methods
    public static MetaData of(Map<String, Object> data) {
        return new MetaData(new HashMap<>(data));
    }

    public static MetaData empty() {
        return new MetaData(new HashMap<>());
    }

    public static MetaData pagination(long totalElements, int totalPages, int page, int size) {
        Map<String, Object> paginationData = new HashMap<>();
        paginationData.put("totalElements", totalElements);
        paginationData.put("totalPages", totalPages);
        paginationData.put("page", page);
        paginationData.put("size", size);
        return new MetaData(paginationData);
    }

    // Getters
    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    public Object get(String key) {
        return data.get(key);
    }
}