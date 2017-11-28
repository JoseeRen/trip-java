package com.cppteam.xcx.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cppteam.common.util.JsonUtils;
import com.cppteam.common.util.TripResult;
import com.cppteam.xcx.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 用户登录拦截器
 * @author happykuan
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private LoginService loginService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
//		// 假如访问的是开放接口，放行
		String uri = request.getRequestURI();
		if ("/xcx/login/getToken".equals(uri) || "/test".equals(uri)) {
			return true;
		}

		// 需要验证登录
		String token = request.getHeader("Authorization");

		if (StringUtils.isNoneBlank(token)) {
			TripResult res = loginService.checkLoginStatus(token);
			if (res.getStatus() == 200) {

				return true;
			}
		}

		// 验证失败，拦截请求并返回错误提示json
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		TripResult badResult = TripResult.build(403, "拒绝访问");
		out.print(JsonUtils.objectToJson(badResult));
		return false;

		// 开发阶段全服放行
//		return true;
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
