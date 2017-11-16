package com.cppteam.common.util;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 请求响应格式工具
 * @author Happykuan
 *
 */
public class TripResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private short status;
	private String message;
	private Object data;

	// 不允许外部通过new关键字实例化，但不是单例
	private TripResult() {}
	
	/**
	 * 返回一个成功的请求处理结果
	 * @param message		成功提示信息
	 * @param data			希望返回的数据
	 * @return				
	 */
	public static TripResult ok(String message, Object data) {

		TripResult response = new TripResult();

		response.status = ResultCode.OK;
		response.message = message;
		response.data = data;
		
		return response;

	}

	/**
	 * 简单版ok
	 * @return
	 */
	public static TripResult ok() {
		TripResult response = new TripResult();

		response.status = ResultCode.OK;
		response.message = "ok";
		response.data = null;

		return response;
	}

	
	/**
	 * 返回一个错误的请求处理结果
	 * @param status		错误状态码
	 * @param message		错误提示信息
	 * @return
	 */
	public static TripResult build(int status, String message) {
		TripResult response = new TripResult();
		response.status = (short)status;
		response.message = message;
		response.data = null;
		
		return response;
	}
	
	/**
	 * 将对象中为null的属性值去掉从而不在json中显示
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T> T formatObject(Object obj, Class<T> clazz) {
		Gson gson = new Gson();
		return gson.fromJson(gson.toJson(obj), clazz);
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public int getStatus() {
		return status;
	}
	
	

}
