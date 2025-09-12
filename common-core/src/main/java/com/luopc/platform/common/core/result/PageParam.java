package com.luopc.platform.common.core.result;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 分页基础参数
 */
@Data
public class PageParam {

    @NotNull(message = "分页参数不能为空")
    private Integer pageNum;
    @NotNull(message = "每页数量不能为空")
    @Max(value = 200, message = "每页最大为200")
    private Integer pageSize;

    protected Boolean searchCount;
    @Size(max = 10, message = "排序字段最多10")
    @Valid
    private List<SortItem> sortItemList;

    /**
     * 排序DTO类
     */
    @Data
    public static class SortItem {
        private Boolean isAsc;
        private String column;
    }
}
