package com.hc.scm.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * Cookie 辅助类
 * @author wu.gy
 * 
 */
public class CookieUtils {

	/**
	 * 获得cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}
	
	/**
	 * 清除cas的登录cookie
	 * @param request
	 */
	public static void clearCasCookie(HttpServletRequest request,HttpServletResponse response) {
		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				System.out.println(c.getName()+","+c.getValue()+","+c.getPath());
				delCookie(response, c.getName());
			}
		}
		System.out.println("---clearCasCookie---");
		cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				System.out.println(c.getName()+","+c.getValue()+","+c.getPath());
			}
		}
	}

	/**
	 * 根据部署路径，将cookie保存在根目录。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * 删除cookie
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void delCookie(HttpServletResponse response, String name) {
		cancleCookie(response, name, null);
	}
	/**
	 * 取消cookie
	 * 
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleCookie(HttpServletResponse response, String name,
			String domain) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		//cookie.setPath("/");
		if (!StringUtils.isBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
}
