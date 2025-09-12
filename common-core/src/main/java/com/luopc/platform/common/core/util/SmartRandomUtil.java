package com.luopc.platform.common.core.util;


import java.util.List;

/**
 * @author Robin
 */
public class SmartRandomUtil {

    public static <T> T randomGet(List<T> collections) {
        if (!collections.isEmpty()) {
            int index = SmartNumberUtil.nextInt(collections.size());
            return collections.get(index);
        }
        return null;
    }

}
