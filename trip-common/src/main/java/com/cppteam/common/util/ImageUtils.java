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
 * Created by happykuan on 2017/10/28.
 */
public class ImageUtils {

    /**
     * 保存网络图片到本地服务器
     * @param url 图片地址
     * @return    上传后的图片地址
     */
    public static String saveImage(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        ByteArrayOutputStream baos = null;
        InputStream in = null;
        try {
            response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
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

                // 上传到图片服务器
                FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastdfs-client.conf");
                String extName = ImageUtils.getImgExtName(url);
                if (StringUtils.isBlank(extName)) {
                    return fastDFSClient.uploadFile(image);
                } else {
                    return fastDFSClient.uploadFile(image, extName);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存字节图片
     * @param bytes
     * @param filename
     * @return
     */
    public static String saveImage(byte[] bytes, String filename) {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastdfs-client.conf");
            String extName = ImageUtils.getImgExtName(filename);

            if (StringUtils.isBlank(extName)) {
                return fastDFSClient.uploadFile(bytes);
            } else {
                return fastDFSClient.uploadFile(bytes, extName);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            return fastDFSClient.deleteFile(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据宽度按比例缩放图片
     * @param imageBytes    源图片byte数组
     * @param formatName    图片的格式
     * @param width         生成目标图片的宽度
     * @return              生成图片的byte数组
     */
    public static byte[] thumbnailImage(byte[] imageBytes, String formatName, int width) {

        formatName = StringUtils.isBlank(formatName) ? "jpg" : formatName;
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

            // 缩放的比例
            double scale = originalWidth / width;
            int height = (int) (originalHeigth / scale);

            // 创建新的缩略图片
            imageThumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics g = imageThumb.getGraphics();
            // 画出图片
            g.drawImage(originalImage, 0, 0, width, height, Color.LIGHT_GRAY, null);
            g.dispose();

            // 缩略图输出为byte[]
            baos = new ByteArrayOutputStream();
            ImageIO.write(imageThumb, formatName, baos);
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response;
        ByteArrayOutputStream baos = null;
        InputStream in = null;
        try {
            response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
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

                // 创建缩略图
                return ImageUtils.thumbnailImage(image, null, width);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}
