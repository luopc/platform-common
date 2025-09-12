package com.luopc.platform.cloud.common.util;

import com.luopc.platform.cloud.common.entity.DeptTreeVO;
import com.luopc.platform.common.core.util.SmartCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class SmartCollectionTest {

    @Test
    public void testBuildTree() {
        //模拟原始数据
        final List<DeptTreeVO> dtos = createMockData();
        //获取树结构
        List<DeptTreeVO> tree = SmartCollection.buildTree(dtos, DeptTreeVO::getId, DeptTreeVO::getParentId, DeptTreeVO::getCreateTime, DeptTreeVO::setChild);

        Map<String, DeptTreeVO> treeVOMap = tree.stream().collect(Collectors.toMap(DeptTreeVO::getDeptCode, e -> e));

        Assertions.assertEquals(Set.of("AA", "AB"), treeVOMap.get("A")
                .getChild().stream()
                .map(DeptTreeVO::getDeptCode)
                .collect(Collectors.toSet()));
        Assertions.assertEquals(Set.of("BB1", "BB2", "BB3", "BB4", "BB5"), treeVOMap.get("B")
                .getChild().stream()
                .map(DeptTreeVO::getDeptCode)
                .collect(Collectors.toSet()));

    }

    private List<DeptTreeVO> createMockData() {
        List<DeptTreeVO> dtos = new ArrayList<>();
        dtos.add(new DeptTreeVO(1L, 0L, "董事长办公室", "A", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(2L, 0L, "党委组织办公室", "B", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(10L, 1L, "总经理办公室", "AA", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(21L, 1L, "股东代表办", "AB", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(11L, 10L, "行政总监", "AA1", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(12L, 10L, "财务总监", "AA2", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(13L, 10L, "运营总监", "AA3", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(14L, 10L, "市场总监", "AA4", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(15L, 10L, "工程总监", "AA5", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(110L, 11L, "综合管理部", "AAA10", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(111L, 11L, "企划部", "AAA11", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(112L, 11L, "品质部", "AAA12", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(120L, 12L, "出纳", "AAA22", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(121L, 12L, "财务", "AAA22", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(130L, 13L, "保洁", "AAA32", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(131L, 13L, "设备", "AAA32", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(140L, 14L, "市场研发", "AAA22", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(141L, 14L, "市场策划", "AAA22", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(150L, 15L, "高空维修", "AAA32", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(151L, 15L, "工程拓展", "AAA32", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(22L, 21L, "股东工会", "AB1", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(23L, 21L, "股东A", "AB2", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(24L, 21L, "股东B", "AB3", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(210L, 22L, "活动策划", "ABA10", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(211L, 22L, "经费管理", "ABA11", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(212L, 22L, "福利小组", "ABA12", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(220L, 23L, "股东出纳A", "ABA22", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(221L, 23L, "股东财务A", "ABA22", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(230L, 23L, "股东出纳B", "ABA32", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(231L, 23L, "股东财务B", "ABA32", LocalDateTime.now(), null));

        dtos.add(new DeptTreeVO(201L, 2L, "党支部", "BB1", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(202L, 2L, "宣传委", "BB2", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(203L, 2L, "组织部", "BB3", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(204L, 2L, "纪检部", "BB4", LocalDateTime.now(), null));
        dtos.add(new DeptTreeVO(205L, 2L, "机关党小组", "BB5", LocalDateTime.now(), null));
        return dtos;
    }

}
