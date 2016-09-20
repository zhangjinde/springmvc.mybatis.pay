package org.yeahka.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yeahka.config.Config;

public abstract class BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected String getBaseUrl(HttpServletRequest request) {
		StringBuilder basepath = new StringBuilder();
		basepath.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort())
				.append(request.getContextPath());

		return basepath.toString();
	}

	protected String getPayPageUrl() {
		return Config.getConfiguration().getString("pay.ui.url");
	}

	protected String getErrorPageUrl() {
		return Config.getConfiguration().getString("pay.ui.error.url");
	}

	/**
	 * 判断是否是微信扫码
	 *
	 * @param request
	 * @return
	 */
	protected boolean is_weixin(HttpServletRequest request) {
		String ua = ((HttpServletRequest) request).getHeader("user-agent");
		if (!StringUtils.isEmpty(ua)) {
			if (ua.toLowerCase().indexOf("micromessenger") > 0) {// 是微信浏览器
				return true;
			}
		}
		return false;
	}

	protected String redirectToErrorPage(String baseUrl, String errorMessage) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		logger.info("baseUrl=" + baseUrl);
		// URLEncoder.encode(errorMessage,"utf-8" )
		String url = String.format("redirect:%s?errorMessage=%s", baseUrl, errorMessage);
		logger.info("getErrorRedirectUrl" + url);
		return url;
	}

	/**
	 * 判断是否是支付宝扫码
	 *
	 * @param request
	 * @return
	 */
	protected boolean is_alipay(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String ua = ((HttpServletRequest) request).getHeader("user-agent");
		if (!StringUtils.isEmpty(ua)) {
			if (ua.toLowerCase().indexOf("AlipayClient".toLowerCase()) > 0) {// 是支付宝浏览器
				return true;
			}
		}
		return false;
	}
}
