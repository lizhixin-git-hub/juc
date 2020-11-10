package com.lzx.common.util.collection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合处理工具类
 */
public class CollectionUtils {

    /**
     * 替换list中最后一个元素
     *
     * @param list 待替换list集合
     * @param t    替换的元素
     * @param <T>  泛型
     * @return 替换后list
     */
    public static <T> List<T> replaceLastElement(List<T> list, T t) {
        return Stream.concat(Stream.concat(
                list.stream().limit(list.size() - 1),
                Stream.of(t)),
                list.stream().skip(list.size())
        ).collect(Collectors.toList());
    }

}
