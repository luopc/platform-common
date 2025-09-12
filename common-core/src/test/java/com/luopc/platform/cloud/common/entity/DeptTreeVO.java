package com.luopc.platform.cloud.common.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptTreeVO {

    private Long id;

    /**
     * 上级部门
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子部门
     */
    private List<DeptTreeVO> child;

}
