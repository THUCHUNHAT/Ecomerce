package com.example.ecomerce.dto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;
@Getter
@Setter
public class PagedResponse<T> {
    private List<T> items;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    public PagedResponse(List<T> items, Page<?> page) {
        this.items = items;
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }
}
