package com.luopc.platform.web.common.core.util;


import java.util.List;

/**
 * @author Robin
 */
public class SimpleRandomUtil {

    public static <T> T randomGet(List<T> collections) {
        if (!collections.isEmpty()) {
            int index = SimpleNumberUtil.nextInt(collections.size());
            return collections.get(index);
        }
        return null;
    }

}
