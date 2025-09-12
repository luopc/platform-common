package com.luopc.platform.common.core.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Page Result
 *
 * @author Platform Team
 * @since 1.0.0
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Current page number
     */
    private long current;

    /**
     * Page size
     */
    private long size;

    /**
     * Total pages
     */
    private long pages;

    /**
     * Total records
     */
    private long total;

    /**
     * Records
     */
    private List<T> records;
    /**
     * Empty flag
     */
    private Boolean emptyFlag;

    public PageResult() {
    }

    public PageResult(long current, long size, long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
        this.pages = (total + size - 1) / size;
    }

    /**
     * Create empty page result
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(1, 0, 0, List.of());
    }

    /**
     * Create page result
     */
    public static <T> PageResult<T> of(long current, long size, long total, List<T> records) {
        return new PageResult<>(current, size, total, records);
    }

    /**
     * Check if has previous page
     */
    public boolean hasPrevious() {
        return current > 1;
    }

    /**
     * Check if has next page
     */
    public boolean hasNext() {
        return current < pages;
    }
}
