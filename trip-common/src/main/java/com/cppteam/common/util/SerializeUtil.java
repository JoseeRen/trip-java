package com.cppteam.common.util;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Java对象序列化工具
 * Created by happykuan on 2017/10/21.
 */
public class SerializeUtil {

    private static final String TEMP_ENCODING = "ISO-8859-1";
    private static final String DEFAULT_ENCODING = "utf-8";

    /**
     * 序列化对象
     * @param o Object
     * @return byte[]
     */
    public static String serialize (Object o){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);

            // 获得序列化字符串并使用utf-8编码
            String str = baos.toString(TEMP_ENCODING);
            return URLEncoder.encode(str, DEFAULT_ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 反序列化精确类型
     * @param serializedObjStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T unSerialize(String serializedObjStr, Class<T> clazz) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            String s = URLDecoder.decode(serializedObjStr, DEFAULT_ENCODING);
            bais = new ByteArrayInputStream(s.getBytes(TEMP_ENCODING));
            ois = new ObjectInputStream(bais);
            T t = (T) ois.readObject();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 反序列化Object
     * @param serializedObjStr
     * @return
     */
    public static Object unSerialize(String serializedObjStr) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            String s = URLDecoder.decode(serializedObjStr, DEFAULT_ENCODING);
            bais = new ByteArrayInputStream(s.getBytes(TEMP_ENCODING));
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
