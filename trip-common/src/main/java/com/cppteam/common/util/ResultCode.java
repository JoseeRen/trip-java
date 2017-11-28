package com.cppteam.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据状态码获取信息
 * @author Kim
 *
 */
public class ResultCode {
	public static final short OK = 200;
	public static final short NOT_FOUND = 404;
	public static final short BAD_REQUEST = 400;
	public static final short BAD_AUTH = 403;
	public static final short SERVER_ERR = 500;
	
	/**
	 * 通过状态码获取状态码对应信息
	 * @param statusCode			状态码
	 * @return
	 */
	public static String getInfo(short statusCode) {
		Map<Short, String> map = new HashMap<Short, String>();
		map.put(ResultCode.OK, "接口使用正常");
		map.put(ResultCode.NOT_FOUND, "查询为空");
		map.put(ResultCode.BAD_REQUEST, "查询参数有误");
		map.put(ResultCode.BAD_AUTH, "验证失败，拒绝访问");
		map.put(ResultCode.SERVER_ERR, "服务器异常");
		
		String info = map.get(statusCode);
		return info == null ? "未知错误" : info;
	}
}
