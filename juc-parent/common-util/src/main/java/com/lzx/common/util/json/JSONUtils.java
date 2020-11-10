package com.lzx.common.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Objects;

public class JSONUtils {

    /**
     * json转List
     *
     * @param str 待转换json格式字符串
     * @param cls 目标类
     * @return 目标对象集合
     */
    public static <T> List<T> getList(String str, Class<T> cls) {
        return JSON.parseArray(str, cls);
    }

    /**
     * 将Json文本数据信息转换为JsonObject对象,获取Value
     *
     * @param key  "name"
     * @param json {"name":"中车信息"}
     * @return "中车信息"
     */
    public static String getValue(String key, String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Object object = jsonObject.get(key);
        if (Objects.isNull(object)) {
            return null;
        }
        return object.toString();
    }

    /**
     * 通过json格式，返回JavaBean对象
     *
     * @param <T>  目标类
     * @param json eg:{"name":"中车信息"}
     * @param cls  User.class
     * @return JavaBean对象
     */
    public static <T> T jsonToObject(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    /**
     * 通过对象转换成json字符串,返回有指定key值的json
     *
     * @param <T> 待转换对象
     * @param key json字符串key
     * @return eg:{"name":"中车信息"}
     */
    public static <T> String toJson(String key, T o) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, o);
        return jsonObject.toString();
    }

    /**
     * 传入对象直接返回Json
     *
     * @param object 待转换对象
     * @return json字符串
     */
    public static <T> String serialize(T object) {
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 将对象转换成Json格式字符串，并过滤多余的字段。
     *
     * @param object 待转换对象
     * @param filter 过滤器
     * @return 过滤后的json格式字符串
     */
    public static <T> String serialize(T object, PropertyFilter filter) {
        return JSON.toJSONString(object, filter, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * json转bean或list
     *
     * @param str           带转换字符串
     * @param typeReference 转换类型
     * @return T 转换后对象
     */
    public static <T> T getObject(String str, TypeReference<T> typeReference) {
        return JSON.parseObject(str, typeReference);
    }

}
