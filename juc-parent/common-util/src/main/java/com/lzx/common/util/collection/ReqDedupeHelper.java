package com.lzx.common.util.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * https://mp.weixin.qq.com/s/uV41OyvcTCL-2uRtLYuw-A
 * 并发重复请求
 */
public class ReqDedupeHelper {

    private static Logger logger = LoggerFactory.getLogger(ReqDedupeHelper.class);

    /**
     *
     * @param reqJSON 请求的参数，这里通常是JSON
     * @param excludeKeys 请求参数里面要去除哪些字段再求摘要
     * @return 去除参数的MD5摘要
     */
    public static String dedupeParamMD5(final String reqJSON, String... excludeKeys) {
        TreeMap<String, String> paramTreeMap = JSON.parseObject(reqJSON, new TypeReference<TreeMap<String, String>>(){});
        if (Objects.nonNull(excludeKeys)) {
            List<String> dedupeExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupeExcludeKeys.isEmpty()) {
                for (String dedupeExcludeKey : dedupeExcludeKeys) {
                    paramTreeMap.remove(dedupeExcludeKey);
                }
            }
        }

        String paramTreeMapJSON = JSON.toJSONString(paramTreeMap);
        String md5deDupParam = jdkMD5(paramTreeMapJSON);
        logger.debug("md5deDupParam = {}, excludeKeys = {} {}", md5deDupParam, Arrays.deepToString(excludeKeys), paramTreeMapJSON);
        return md5deDupParam;
    }

    private static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            logger.error(StringUtils.EMPTY,e);
        }
        return res;
    }

    public static void main(String[] args) {
            //两个请求一样，但是请求时间差一秒
            String req = "{\n" +
                    "\"requestTime\" :\"20190101120001\",\n" +
                    "\"requestValue\" :\"1000\",\n" +
                    "\"requestKey\" :\"key\"\n" +
                    "}";

            String req2 = "{\n" +
                    "\"requestTime\" :\"20190101120002\",\n" +
                    "\"requestValue\" :\"1000\",\n" +
                    "\"requestKey\" :\"key\"\n" +
                    "}";

            //全参数比对，所以两个参数MD5不同
            String dedupeMD5 = dedupeParamMD5(req);
            String dedupeMD52 = dedupeParamMD5(req2);
            System.out.println("req1MD5 = "+ dedupeMD5+" , req2MD5="+dedupeMD52);

            //去除时间参数比对，MD5相同
            String dedupeMD53 = dedupeParamMD5(req,"requestTime");
            String dedupeMD54 = dedupeParamMD5(req2,"requestTime");
            System.out.println("req1MD5 = "+ dedupeMD53+" , req2MD5="+dedupeMD54);

    }

}
