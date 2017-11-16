package com.cppteam.utils;

import java.io.Serializable;

/**
 * layui数据表格返回json格式
 * Created by happykuan on 2017/11/7.
 * @author happykuan
 */
public class TableResult implements Serializable{
    private static final long serialVersionUID = -5616443554701912194L;
    private Integer code;
    private String msg;
    private Long count;
    private Object data;

    public TableResult() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
