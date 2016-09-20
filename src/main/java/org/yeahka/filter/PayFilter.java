package org.yeahka.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayFilter implements Filter {

	private final static Logger logger=LoggerFactory.getLogger(PayFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request1 = (HttpServletRequest) request;

		java.util.Enumeration requestParamNames =  request.getParameterNames();
		StringBuffer sb = new StringBuffer();
		while (requestParamNames.hasMoreElements()) {
			String key = (String)requestParamNames.nextElement();
			if (sb.length() == 0) sb.append("?"); else sb.append("&");
			sb.append(key).append("=");// + request.getParameter(key);
			String[] arr = request.getParameterValues(key);
			StringBuffer value = new StringBuffer();
			for (int i=0; i<arr.length; i++) {
				if (i > 0) value.append(",");
				value.append(arr[i]);
			}
			sb.append(value);
		}

		String url = request1.getScheme()+"://"+request1.getServerName()+":"+request1.getServerPort()+request1.getContextPath()+request1.getServletPath()+sb.toString();

		String userAgent=request1.getHeader("USER-AGENT");
		if(null==userAgent)
		{
			userAgent="";
		}
		logger.info("userAgent="+userAgent+" "+url);


		HttpServletResponse httpResponse = (HttpServletResponse) response;

		/**
		 * Access-Control-Allow-Origin: *              # 允许所有域名访问，或者
Access-Control-Allow-Origin: http://a.com   # 只允许所有域名访问
		 */
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");//cross domain request/CORS


		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
