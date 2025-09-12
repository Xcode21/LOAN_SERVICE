package com.xcode.loanservice.model.common.exception.vo;

import lombok.Data;

import java.util.List;


@Data
public class PaginatedResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
    private final int size;
    private final boolean hasNext;
    private final boolean hasPrevious;

    public PaginatedResponse(List<T> content, long totalElements, PageRequest pageRequest) {
        this.content = content!=null?content: List.of();
        this.totalElements = totalElements;
        this.size = pageRequest.getSize();
        this.currentPage = pageRequest.getPage();
        this.totalPages = size>0?(int) Math.ceil((double) totalElements / size):0;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }

    public PageRequest getPageRequest() {
        return new PageRequest(currentPage, size,"fecha_creacion","DESC");
    }
}
