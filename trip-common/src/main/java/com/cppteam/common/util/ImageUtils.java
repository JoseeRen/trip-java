package com.cppteam.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 图片处理工具，提供图片的验证、缩略上传、删除等功能
 *
 * @author happykuan
 * @date 2017/10/28
 */
public class ImageUtils {

    /**
     * 保存字节图片
     * @param bytes
     * @param filename
     * @return
     */
    public static String saveImage(byte[] bytes, String filename) {
        FastDFSClient fastDFSClient = null;
        try {
            fastDFSClient = new FastDFSClient("classpath:properties/fastdfs-client.conf");
            String extName = ImageUtils.getImgExtName(filename);
            String fileId = "";
            if (StringUtils.isBlank(extName)) {
                fileId = fastDFSClient.uploadFile(bytes);
            } else {
                fileId = fastDFSClient.uploadFile(bytes, extName);
            }

            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            fastDFSClient = null;
        }
    }

    /**
     * 保存网络图片到本地服务器
     * @param url 图片地址
     * @return    上传后的图片地址
     */
    public static String saveImage(String url) {

        byte[] image = urlToBytes(url);
        if (image != null) {
            return saveImage(image, url);
        } else {
            return null;
        }

    }

    /**
     * 删除图片
     * @param fileId
     * @return
     */
    public static int deleteImage(String fileId) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastdfs-client.conf");
            int i = fastDFSClient.deleteFile(fileId);
            fastDFSClient = null;
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据宽度按比例缩放图片, 生成压缩的jpg格式图
     * @param imageBytes    源图片byte数组
     * @param width         生成目标图片的宽度
     * @return              生成图片的byte数组
     */
    public static byte[] thumbnailImage(byte[] imageBytes, int width) {


        // formatName = StringUtils.isBlank(formatName) ? "jpg" : formatName;
        String formatName = "jpg";
        InputStream is = null;
        BufferedImage originalImage;
        BufferedImage imageThumb;
        ByteArrayOutputStream baos = null;
        try {
            is = new ByteArrayInputStream(imageBytes);
            originalImage = ImageIO.read(is);
            // 原始宽度
            int originalWidth = originalImage.getWidth();
            // 原始高度
            int originalHeigth = originalImage.getHeight();

            // 缩放的比例, ！important 运算精度要到double，否则一些图片无法成功按比例缩放
            double scale = (double) originalWidth / (double) width;

            Long height = Math.round((double) originalHeigth / scale);

            // 创建新的缩略图片
            imageThumb = new BufferedImage(width, height.intValue(), BufferedImage.TYPE_USHORT_555_RGB);
            Graphics g = imageThumb.getGraphics();
            // 画出图片
            g.drawImage(originalImage, 0, 0, width, height.intValue(), Color.LIGHT_GRAY, null);
            g.dispose();

            // 缩略图输出为byte[]
            baos = new ByteArrayOutputStream();
            ImageIO.write(imageThumb, formatName, baos);
            baos.flush();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 创建网络图片的缩略图
     * @param url
     * @return
     */
    public static byte[] thumbnailImage(String url, Integer width) {
        byte[] image = urlToBytes(url);
        if (image == null) {
            return null;
        }

        return thumbnailImage(image, width);
    }

    /**
     * 获得图片的后缀名，不包含点“.”，假如后缀名不存在，则返回jpg
     * @param filename  图片名称
     * @return
     */
    public static String getImgExtName(String filename) {
        // 获得待检测的拓展名
        if (filename == null) {
            return null;
        }
        int i = filename.lastIndexOf(".");
        if (i == -1) {
            return null;
        }
        String extName = filename.substring(i + 1, filename.length());
        // 检测扩展名，如果不合法，则默认返回null
        extName = ImageUtils.isLegalExtName(extName) ? extName : null;
        return extName;
    }

    /**
     * 检测服务器是否支持该种图片后缀名
     * @param filename
     * @return
     */
    public static boolean isLegalFilename(String filename) {
        if (filename == null) {
            return false;
        }
        int i = filename.lastIndexOf(".");
        if (i == -1) {
            return false;
        }
        String extName = filename.substring(i + 1, filename.length());

        String types = Arrays.toString(ImageIO.getReaderFormatNames());
        return types.toLowerCase().contains(extName.toLowerCase());
    }

    /**
     * 检测图片后缀名是否合法
     * @param extName 待检测后缀名
     * @return
     */
    private static boolean isLegalExtName(String extName) {
        String types = Arrays.toString(ImageIO.getReaderFormatNames());
        if (extName == null || !types.toLowerCase().contains(extName.toLowerCase())){
            return false;
        }
        return true;
    }

    /**
     * 获取网络图片byte数组
     * @param url
     * @return
     */
    private static byte[] urlToBytes(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        ByteArrayOutputStream baos = null;
        InputStream in = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 图片转换为字节数组
                in = response.getEntity().getContent();

                byte[] bytes = new byte[1024];
                int len = 0;
                baos = new ByteArrayOutputStream();
                while((len = in.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                    baos.flush();
                }
                byte[] image = baos.toByteArray();

                return image;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
