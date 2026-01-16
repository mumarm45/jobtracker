package com.sample.jobtracker.model;

import java.util.List;

public class PaginatedResponse<T> {
    private List<T> data;
    private long total;
    private Integer next;
    private int offset;
    private int limit;

    public PaginatedResponse() {
    }

    public PaginatedResponse(List<T> data, long total, int offset, int limit) {
        this.data = data;
        this.total = total;
        this.offset = offset;
        this.limit = limit;
        this.next = calculateNext(total, offset, limit);
    }

    private Integer calculateNext(long total, int offset, int limit) {
        int nextOffset = offset + limit;
        return nextOffset < total ? nextOffset : null;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
