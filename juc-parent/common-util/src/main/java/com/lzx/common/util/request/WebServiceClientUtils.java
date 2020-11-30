package com.lzx.common.util.request;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebService接口调用工具类
 */
public class WebServiceClientUtils {

    private static Logger logger = LoggerFactory.getLogger(WebServiceClientUtils.class);


    /**
     * json参数请求省网上传数据接口
     *
     * @param address 省网请求地址
     * @param method  调用方法
     * @param params  请求参数
     * @return 请求结果
     */
    public static Object[] getWebService(String address, String method, Object params) {
        //创建动态代理
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();

        //获取客户端实体
        Client client = factory.createClient(address);

        //请求结果接收
        Object[] objects = null;

        try {
            //调用webservice，获取结果
            objects = client.invoke(method, params);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("webservice调用失败！！！！");
        }

        //返回结果
        return objects;
    }
}
