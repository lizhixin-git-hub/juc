package com.lzx.common.util.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * https://mp.weixin.qq.com/s/Ub-43yBs66m_HiYjySuyOg
 */
public class ImgDeal {
    private final static int FONTSIZE = 24;

    /**
     * 绘制图片
     * @param filePath 文件路径
     * @param outStream 输出流
     * @param characters 字符集
     * @param heights 高
     * @param c 颜色
     * @throws IOException 异常
     *
     */
    public static void drawImg(String filePath, OutputStream outStream, ArrayList<String> characters, ArrayList<Integer> heights, Color c) throws IOException {
        //参数不对，直接返回
        if(Objects.isNull(characters) || Objects.isNull(heights) || !Objects.equals(characters.size(), heights.size())){
            return;
        }

        File file = new File(filePath);
        //文件不存在，直接返回
        if (!file.exists()) {
            return;
        }

        Image img = ImageIO.read(file);
        int[] wh = getImgWH(img);
        BufferedImage bufferedImage = new BufferedImage(wh[0], wh[1], BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(img.getScaledInstance(wh[0], wh[1], Image.SCALE_SMOOTH), 0, 0, null);
        g.setColor(c);
        int i = 0;

        //图片上输入文字
        for (String character : characters){
            int[] fl = getFsLs(wh[0], getLength(character));
            g.setFont(new Font("楷体", Font.BOLD, fl[1]));
            g.drawString(character, fl[0], heights.get(i++));
        }

        //将图片输入到输出流中
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outStream);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
        param.setQuality(1.0F, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(bufferedImage);
    }

    /**
     * 计算字符长度
     * @param question 问题
     * @return 字符长度
     */
    private static int getLength(String question){
        int re = 0;
        if (question != null){
            int count = question.length();
            for(int i = 0; i < count; i++) {
                if (question.charAt(i) < 255) {
                    re += 5;
                } else {
                    re += 9;
                }
            }
        }
        re /= 9;
        return re;
    }

    /**
     * 获取图片的长宽
     * @param img 图片
     * @return 图片的长宽
     */
    private static int[] getImgWH(Image img){
        int[] wh = new int[2];
        wh[0] = img.getWidth(null);
        wh[1] = img.getHeight(null);
        return wh;
    }

    /**
     * 计算生成文字的宽度的起始位置和字体大小
     * @param width 宽
     * @param length 长
     * @return 生成文字的宽度的起始位置和字体大小
     */
    private static int[] getFsLs(int width, int length) {
        int[] fl = new int[2];
        if (length * FONTSIZE < width) {
            fl[0] = (width - (length * FONTSIZE)) / 2;
            fl[1] = FONTSIZE;
        } else {
            fl[0] = 5;
            fl[1] = width / length - 1;
        }
        return fl;
    }

}
