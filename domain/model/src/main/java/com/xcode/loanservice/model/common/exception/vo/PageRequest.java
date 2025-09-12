package com.xcode.loanservice.model.common.exception.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageRequest {
    private final int page;
    private final int size;
    private final String sortBy;
    private final String sortDirection;

    public PageRequest(int page, int size, String sortBy, String sortDirection) {
        this.page = Math.max(0, page);
        this.size = Math.min(Math.max(1, size), 100);
        this.sortBy= sortBy!= null ? sortBy : "fecha_creacion";
        this.sortDirection= sortDirection!= null ? sortDirection.toUpperCase() : "DESC";
    }

    public long getOffset() {
        return (long) page * size;
    }
}
