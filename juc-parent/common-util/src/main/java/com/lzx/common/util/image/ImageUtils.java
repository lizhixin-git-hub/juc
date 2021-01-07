package com.lzx.common.util.image;

import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class ImageUtils {

    /**
     * 获取图片base64字符串
     *
     * @param imgUrl 图片路径
     * @return 图片base64字符串
     */
    private static String imageToBase64(String imgUrl) {
        URL url;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            //文件大于1024kb
            if (httpUrl.getContentLength() >= 1024 * 1024) {
                outStream = new ByteArrayOutputStream();
                Thumbnails.of(is).scale(1d).outputQuality((1024 * 1024d) / httpUrl.getContentLength()).toOutputStream(outStream);
                //对字节数组Base64编码
                return Base64.getEncoder().encodeToString(outStream.toByteArray());
            } else {
                outStream = new ByteArrayOutputStream();
                // 创建一个Buffer字符串  
                byte[] buffer = new byte[1024];
                // 每次读取的字符串长度，如果为-1，代表全部读取完毕
                int len;
                // 使用一个输入流从buffer里把数据读取出来
                while ((len = is.read(buffer)) != -1) {
                    // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                    outStream.write(buffer, 0, len);
                }
                // 对字节数组Base64编码
                return Base64.getEncoder().encodeToString(outStream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String imgUrl = "http://img.ddqnr.com/nnimg/imgs/2017/01/ent_expand_134556639622_img1_org.jpg";
        System.out.println(imageToBase64(imgUrl));
    }

}
