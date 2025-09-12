package com.luopc.platform.common.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmartCollection {


    /**
     * 构造数据树 O(N)
     *
     * @param list           原集合
     * @param mKey           父级被子集关联的字段,比如id
     * @param treeConnectKey 子集关联父级的字段，比如parentId
     * @param treeSortKey    同级菜单的排序字段，比如sort
     * @param consumer       添加子集的函数，如dto.setChild(list)
     * @param <M>
     * @return
     */
    public static <M> List<M> buildTree(List<M> list, Function<M, ?> mKey, Function<M, ?> treeConnectKey,
                                        Function<M, ? extends Comparable> treeSortKey, Consumers<M, M> consumer) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Assert.notNull(mKey, "父级被子级关联的字段不能为空");
        Assert.notNull(treeConnectKey, "子级关联父级的字段不能为空");
        Assert.notNull(consumer, "消费函数不能为空");
        //线程安全类集合
        List<M> tree = Collections.synchronizedList(new ArrayList<>());
        //按上级id分组
        final Map<?, List<M>> collect = list.parallelStream().collect(Collectors.groupingBy(treeConnectKey));
        list.parallelStream().filter(m -> {
            final boolean b = (Long) treeConnectKey.apply(m) != 0L;
            if (!b) {
                tree.add(m);
            }
            return b;
        }).forEach(
                //通过对象地址引用实现父子级关联，即使父级先添加了子级，子级在添加孙子级，父子孙三级也全部都会关联
                m -> {
                    if (treeSortKey != null) {
                        consumer.accept(m, CollUtil.sort(collect.get(mKey.apply(m)), Comparator.comparing(treeSortKey)));
                    } else {
                        consumer.accept(m, collect.get(mKey.apply(m)));
                    }
                }
        );
        if (treeSortKey != null) {
            //排序
            tree.sort(Comparator.comparing(treeSortKey));
            return tree.parallelStream().peek(b -> consumer.accept(b, CollUtil.sort(collect.get(mKey.apply(b)), Comparator.comparing(treeSortKey)))).collect(Collectors.toList());
        } else {
            return tree.parallelStream().peek(b -> consumer.accept(b, collect.get(mKey.apply(b)))).collect(Collectors.toList());
        }
    }

    @FunctionalInterface
    public interface Consumers<M, N> {

        /**
         * 消费函数
         *
         * @param m
         * @param n
         */
        void accept(M m, List<N> n);
    }
}

