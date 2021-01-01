package com.lzx.common.util.generate;

import org.apache.commons.lang3.RandomUtils;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://mp.weixin.qq.com/s/5LIkqIppN0HKFxA2eKQ86g
 */
public class OrderGenerateUtils {
    /**
     * 订单号生成
     **/
    private static ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    private static final AtomicInteger SEQ = new AtomicInteger(1000);

    private volatile static String IP_SUFFIX = null;

    private static final DateTimeFormatter DF_FMT_PREFIX = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    public static String generateOrderNo() {
        LocalDateTime dataTime = LocalDateTime.now(ZONE_ID);
        if (SEQ.intValue() > 9990) {
            SEQ.getAndSet(1000);
        }
        return dataTime.format(DF_FMT_PREFIX) + getLocalIpSuffix() + SEQ.getAndIncrement();
    }

    private static String getLocalIpSuffix() {
        if (Objects.nonNull(IP_SUFFIX)) {
            return IP_SUFFIX;
        }
        try {
            synchronized (OrderGenerateUtils.class) {
                if (Objects.nonNull(IP_SUFFIX)) {
                    return IP_SUFFIX;
                }
                InetAddress addr = InetAddress.getLocalHost();
                //  172.17.0.4  172.17.0.199 ,
                String hostAddress = addr.getHostAddress();
                if (Objects.nonNull(hostAddress) && hostAddress.length() > 4) {
                    String ipSuffix = hostAddress.trim().split("\\.")[3];
                    if (ipSuffix.length() == 2) {
                        IP_SUFFIX = ipSuffix;
                        return IP_SUFFIX;
                    }
                    ipSuffix = "0" + ipSuffix;
                    IP_SUFFIX = ipSuffix.substring(ipSuffix.length() - 2);
                    return IP_SUFFIX;
                }
                IP_SUFFIX = Objects.toString(RandomUtils.nextInt(10, 20));
                return IP_SUFFIX;
            }
        } catch (Exception e) {
            System.out.println("获取IP失败:" + e.getMessage());
            IP_SUFFIX = Objects.toString(RandomUtils.nextInt(10, 20));
            return IP_SUFFIX;
        }
    }


    public static void main(String[] args) {
        List<String> orderNos = Collections.synchronizedList(new ArrayList<>());
        IntStream.range(0, 8000).parallel().forEach(i -> orderNos.add(generateOrderNo()));

        List<String> filterOrderNos = orderNos.stream().distinct().collect(Collectors.toList());

        System.out.println("订单样例：" + orderNos.get(22));
        System.out.println("生成订单数：" + orderNos.size());
        System.out.println("过滤重复后订单数：" + filterOrderNos.size());
        System.out.println("重复订单数：" + (orderNos.size() - filterOrderNos.size()));
    }
}