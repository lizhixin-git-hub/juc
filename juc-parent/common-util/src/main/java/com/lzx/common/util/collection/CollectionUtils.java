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

    /**
     * 获取两个list中的不同元素
     *
     * @param list1 集合1
     * @param list2 集合2
     * @return 不同的元素集合
     */
    public List<String> getDifferentElements(List<String> list1, List<String> list2) {
        //创建存放不同元素集合
        List<String> diff = new ArrayList<>();

        //创建存储临时数据map
        Map<String, Integer> map = new HashMap<>(list1.size() + list2.size());

        //定义最大集合
        List<String> maxList = list1;

        //定义最小集合
        List<String> minList = list2;

        //获取最大集合与最小集合
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }

        //循环最大集合
        for (String string : maxList) {
            map.put(string, 1);
        }

        //循环最小集合
        for (String string : minList) {
            Integer count = map.get(string);
            if (count != null) {
                map.put(string, ++count);
                continue;
            }
            map.put(string, 1);
        }

        //获取不同的集合
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }

        //返回不同集合
        return diff;
    }

}
