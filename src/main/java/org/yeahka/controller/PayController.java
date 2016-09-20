//package org.yeahka.controller;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.security.InvalidParameterException;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//import org.yeahka.commons.utils.JsonUtil;
//import org.yeahka.commons.utils.MD5;
//
//
//@Controller
//public class PayController {
////	static final Log logger = LogFactory.getLog(new com.yeahka.common.BaseLog() {}.getClassName());
//
//	private final static Logger logger=LoggerFactory.getLogger(PayController.class);
//
//	/** 商户支付时进行签名验证的key */
//	private final static String SIGN_KEY = "MNBVCXZQWEEASLKDFJKLAJFQWNE23IFK";
//
//
//
////	/**
////	 * 提供给app调用，用来获取带签名的支付请求地址
////	 * @param request
////	 * @param response
////	 * @param merchantId 商户ID
////	 * @param username 用户名
////	 * @return
////	 * @throws UnsupportedEncodingException
////	 */
////	@RequestMapping(value = "/merchant_shop_url", method = RequestMethod.GET)
////	@ResponseBody
////	public String getMerchantShopUrl(HttpServletRequest request,HttpServletResponse response,
////		String merchantId, String username) throws UnsupportedEncodingException {
////		Map<String, Object> map = new HashMap<String, Object>();
////		map.put("code", "-1");
////		map.put("url","");
////		if (StringUtils.isEmpty(merchantId) || "null".equals(merchantId)) {
////			map.put("message", "merchantId不能为空");
////			return JsonUtil.toJson(map);
////		}
////		if (StringUtils.isEmpty(username) || "null".equals(username)) {
////			map.put("message", "username不能为空");
////			return JsonUtil.toJson(map);
////		}
////		String sign = MD5.md5(SIGN_KEY + merchantId).toUpperCase();
////		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
////				+ "/o2o/pay/merchant_shop.do?merchantId="+merchantId+"&username="+username+"&sign="+sign;
////		map.put("code", "0");
////		map.put("message", "ok");
////		map.put("url",url);
////		return JsonUtil.toJson(map);
////	}
////
////	/**
////	 * 新增的带签名认证的支付请求接口
////	 * @param request
////	 * @param response
////	 * @param modelAndView
////	 * @param merchantId
////	 * @param t0
////	 * @param amount
////	 * @param sign
////	 * @return
////	 * @throws UnsupportedEncodingException
////	 */
////	@RequestMapping(value = "/merchant_shop", method = RequestMethod.GET)
////	public ModelAndView merchant_shop(HttpServletRequest request,HttpServletResponse response,
////		ModelAndView modelAndView,String merchantId, String t0, String amount, String sign) throws UnsupportedEncodingException {
////		logger.info("merchant_shop--t0="+t0+",amount="+amount+",sign="+sign);
////		try
////		{
////			if(StringUtils.isEmpty(merchantId))
////			{
////				throw new InvalidParameterException("merchantId can not null");
////			}
////
////			String md5Sign = MD5.md5(SIGN_KEY + merchantId).toUpperCase();
////			if(!md5Sign.equals(sign)) {
////				throw new InvalidParameterException("sign error");
////			}
////
////			String username	= request.getParameter("username");
////
////			  ShopModel shop=serviceFacade.getShopByMerchantId(merchantId);
////			  if(shop==null)
////			  {
////					logger.info("can not find shop ,auto create");
////					shop=serviceFacade.createShop(merchantId, username);
////			  }
////
////
////			  if (!is_weixin(request)) {// 不是微信浏览器
////				  modelAndView.setViewName("redirect:/pay/no_supported.do");
////				  return modelAndView;
////			  }
////
////			String o2oToken = "";
//////			O2OSession o2oSession=getO2OSession(request, null);
//////			if(o2oSession!=null)
//////			{
//////				o2oToken=o2oSession.getToken();
//////			}
////
////			O2OSession o2oSession=getOrCreateO2OSession(request, response);
////			o2oToken=o2oSession.getToken();
////			logger.info("o2oToken = "+o2oToken);
////
////			logger.info("request o2oToken = "+o2oToken);
////			String callback = "/pay/pay_weixin.do";
////
////			String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
////					+ "/o2o/pay/getOpenId.do?callback="+callback+"&merchantId="+merchantId+"&username="+username
////					+"&o2oToken="+o2oToken+"&t0="+t0+"&amount="+amount;
////			String url = java.net.URLEncoder.encode(webUrl, "utf-8");
////			logger.info(url);
////
////			logger.info("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
////			modelAndView.setViewName("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
////
////		   	return modelAndView;
////		}
////		catch(Exception e)
////		{
////			logger.info(e.getMessage());
////			e.printStackTrace();
////			throw e;
////		}
////	}
//
//	@RequestMapping(value = "/pay_weixin_userinfo", method = RequestMethod.GET)
//	public ModelAndView pay_weixin_userinfo(HttpServletRequest request,HttpServletResponse response,
//		ModelAndView modelAndView,String merchantId, String t0, String amount) throws UnsupportedEncodingException {
//		logger.info("test get clientAddr=="+InternetProtocolUtil.getRemoteAddr(request));
//		try
//		{
//
//			if(StringUtils.isEmpty(merchantId))
//			{
//				throw new InvalidParameterException("merchantId can not null");
//			}
//
//			String username	= request.getParameter("username");
//
//			  ShopModel shop=serviceFacade.getShopByMerchantId(merchantId);
//			  if(shop==null)
//			  {
//					logger.info("can not find shop ,auto create");
//					shop=serviceFacade.createShop(merchantId, username);
//			  }
//
//
//			  if (!is_weixin(request)) {// 不是微信浏览器
//				  modelAndView.setViewName("redirect:/pay/no_supported.do");
//				  return modelAndView;
//			  }
//
//
//
//			String o2oToken = "";
////			O2OSession o2oSession=getO2OSession(request, null);
////			if(o2oSession!=null)
////			{
////				o2oToken=o2oSession.getToken();
////			}
//
//			O2OSession o2oSession=getOrCreateO2OSession(request, response);
//			o2oToken=o2oSession.getToken();
//			logger.info("o2oToken = "+o2oToken);
//
//			logger.info("request o2oToken = "+o2oToken);
//			String callback = "/pay/pay_weixin.do";
//
//			String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//					+ "/o2o/pay/getOpenId.do?callback="+callback+"&merchantId="+merchantId+"&username="+username
//					+"&o2oToken="+o2oToken+"&t0="+t0+"&amount="+amount;
//			String url = java.net.URLEncoder.encode(webUrl, "utf-8");
//			logger.info(url);
//
//			logger.info("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
//			modelAndView.setViewName("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
//
//		   	return modelAndView;
//		}
//		catch(Exception e)
//		{
//			logger.info(e.getMessage());
//			e.printStackTrace();
//			throw e;
//		}
//
//
//	}
//
//	private boolean is_weixin(HttpServletRequest request) {
//
//		String ua = ((HttpServletRequest) request).getHeader("user-agent");
//		if(!StringUtils.isEmpty(ua))
//		{
//			ua=ua.toLowerCase();
//		}
//		else
//		{
//			ua="";
//		}
//
//		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
//	        return true;
//	      }
//		 return false;
//	}
//
//	@RequestMapping(value = "/getOpenId", method = RequestMethod.GET)
//	public ModelAndView getOpenId(HttpServletRequest request,HttpServletResponse response,
//			ModelAndView modelAndView,String callback,String merchantId,String o2oToken,
//			String t0, String amount) throws IOException {
//		logger.info("o2oToken = "+o2oToken);
//		try
//		{
//			String code = request.getParameter("code");
//			WxUserInfoModel userInfo = getUserInfo_base(code);
//			if(userInfo==null) {
//				logger.info("getOpenId -> getSessionIdFail");
//				modelAndView.setViewName("redirect:http://www.yeahka.com");
//				return modelAndView;
//			}
//			logger.info("getOpenId():userInfo"+userInfo);
//
//			String orderId = request.getParameter("orderId");
//			if(orderId==null) {
//				orderId = "";
//			}
//
//			String username = request.getParameter("username");
//			logger.info("username = "+username);
//			Map<String,Object> map = getPayService().queryUnionId(userInfo.getOpenId(),1);
//			String uniResult = map.get("result").toString();
//			if(!uniResult.equals("0")) {
//				//用非静默授权
//				String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/o2o/pay/getOpenId_userinfo.do?callback="+callback+"&merchantId="
//				+merchantId+"&username="+username+"&o2oToken="+o2oToken+"&orderId="+orderId+"&t0="+t0+"&amount="+amount;
//				String url = java.net.URLEncoder.encode(webUrl, "utf-8");
//				modelAndView.setViewName("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url +
//						"&response_type=code&scope=snsapi_userinfo&state=123&connect_redirect=1#wechat_redirect");
//				return modelAndView;
//			}else {
//				userInfo.setUnionid(map.get("unionid").toString());
//			}
//
//			Map<String,String> loginMap = getPayService().loginLeshua(userInfo.getOpenId(), userInfo.getUnionid(),1);
//			logger.info(""+loginMap);
//			if(!loginMap.get("root.error_code").equals("0")) {
//				logger.info("getOpenId loginMap -> getSessionIdFail");
//				modelAndView.setViewName("redirect:http://www.yeahka.com");
//				return modelAndView;
//			}
//
//			Double weiXinPayAmount=0.0;
//			if(StringUtil.isNotEmpty(orderId)) {
//				Map<String, Object> mm = getPayService().queryOrderByOrderId(orderId);
//				if(mm!=null && mm.get("F_amount")!=null) {
//					weiXinPayAmount=Double.valueOf(mm.get("F_amount").toString());
//				}
//			}
//
//			String login_session_id = loginMap.get("root.session_id");
//			String login_uin = loginMap.get("root.uin");
//			String login_uid = loginMap.get("root.uid");
//
//			//O2OSession o2oSession=getO2OSession(request, response);
//			SessionService sessionService=(SessionService) ConsumerFactory.getBean("sessionService");
//			O2OSession o2oSession=sessionService.getO2OSessionByToken(o2oToken);
//			if(o2oSession==null)
//			{
//				o2oSession=getOrCreateO2OSession(request, response);
//				o2oToken=o2oSession.getToken();
//			}
//			logger.info("merchantId = "+merchantId);
//			o2oSession.setAttribute(O2OSession.YKUinCookieName, login_uin);
//			o2oSession.setAttribute(O2OSession.YKUidCookieName, login_uid);
//			o2oSession.setAttribute(O2OSession.YKSessionIdCookieName, login_session_id);
//
//			logger.info("token = "+o2oSession!=null?o2oSession.getToken():"");
//			o2oSession.setAttribute("weixinpay_merchantId", merchantId);
//			o2oSession.setAttribute("weixinpay_username", username);
//			o2oSession.setAttribute("weixinpay_userinfo", userInfo);
//			o2oSession.setAttribute("weixinpay_uid", login_uid);
//			o2oSession.setAttribute("weixinpay_uin", login_uin);
//			o2oSession.setAttribute("weixinpay_leshua_order_id", orderId);
//			o2oSession.setAttribute("weixinpay_amount", weiXinPayAmount);
//			sessionService.saveSession(o2oSession);
//
//			String decode = callback;
//			logger.info("decode:"+decode);
//			modelAndView.setViewName("redirect:"+decode+"?o2oToken="+o2oToken+"&t0="+t0+"&amount="+amount);
//			return modelAndView;
//		}
//		catch(Exception e)
//		{
//			logger.info(e.getMessage());
//			e.printStackTrace();
//			throw e;
//		}
//	}
//
//	@RequestMapping(value = "/getOpenId_userinfo", method = RequestMethod.GET)
//	public ModelAndView getOpenId_userinfo(HttpServletRequest request,HttpServletResponse response,
//			ModelAndView modelAndView,String callback,String merchantId,String o2oToken,
//			String t0, String amount) throws IOException {
//		logger.info("o2oToken = "+o2oToken);
//		String code = request.getParameter("code");
//		WxUserInfoModel userInfo = getUserInfo_userinfo(code);
//		logger.info("getOpenId():userInfo"+userInfo);
//
//
//		Map<String, String> loginMap = getPayService().loginLeshua(userInfo.getOpenId(), userInfo.getUnionid(), 1);
//		logger.info(""+loginMap);
//		if(!loginMap.get("root.error_code").equals("0")) {
//			logger.info("getOpenId_userinfo -> getSessionIdFail");
//			modelAndView.setViewName("redirect:http://www.yeahka.com");
//		}
//		logger.info("o2oToken = "+o2oToken);
//
//
//		getPayService().saveUserInfo(userInfo,loginMap.get("root.uin"));
//
//		String orderId = request.getParameter("orderId");
//
//		Double weiXinPayAmount=0.0;
//		if(StringUtil.isNotEmpty(orderId)) {
//			Map<String, Object> mm = getPayService().queryOrderByOrderId(orderId);
//			if(mm!=null && mm.get("F_amount")!=null) {
//				weiXinPayAmount=Double.valueOf(mm.get("F_amount").toString());
//			}
//		}
//
//		String login_session_id = loginMap.get("root.session_id");
//		String login_uin = loginMap.get("root.uin");
//		String login_uid = loginMap.get("root.uid");
//
//		SessionService sessionService=(SessionService) ConsumerFactory.getBean("sessionService");
//		O2OSession o2oSession=sessionService.getO2OSessionByToken(o2oToken);
//
//		String username = request.getParameter("username");
//		if(username == null) {
//			logger.info("username is null");
//			username = "";
//		}
//
//
//		if(o2oSession==null)
//		{
//			o2oSession=getOrCreateO2OSession(request, response);
//			o2oToken=o2oSession.getToken();
//		}
//
//		o2oSession.setAttribute(O2OSession.YKUinCookieName, login_uin);
//		o2oSession.setAttribute(O2OSession.YKUidCookieName, login_uid);
//		o2oSession.setAttribute(O2OSession.YKSessionIdCookieName, login_session_id);
//		o2oSession.setAttribute("weixinpay_merchantId", merchantId);
//		o2oSession.setAttribute("weixinpay_username", username);
//		o2oSession.setAttribute("weixinpay_userinfo", userInfo);
//		o2oSession.setAttribute("weixinpay_uid", login_uid);
//		o2oSession.setAttribute("weixinpay_uin", login_uin);
//		o2oSession.setAttribute("weixinpay_leshua_order_id", orderId);
//		o2oSession.setAttribute("weixinpay_amount", weiXinPayAmount);
//		sessionService.saveSession(o2oSession);
//
//
//		String decode = callback;
//		logger.info("decode:"+decode);
//		modelAndView.setViewName("redirect:"+decode+"?o2oToken="+o2oToken+"&t0="+t0+"&amount="+amount);
//		return modelAndView;
//	}
//
//	public WxUserInfoModel getUserInfo_base(String code) {
//		WxUserInfoModel userInfo = new WxUserInfoModel();
//		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Config.appIdBusiness + "&secret=" + Config.appSecretBusiness + "&code=" + code + "&grant_type=authorization_code";
//		String openId = "";
//
//		try {
//			JSONObject json = NetUtil.getJson(url);
//			logger.info(""+json);
//			openId = json.getString("openid");
//
//			userInfo.setOpenId(openId);
//			if(json.has("unionid")){
//				userInfo.setUnionid(json.getString("unionid"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			userInfo = null;
//			logger.error("e",e);
//		}
//		return userInfo;
//	}
//
//	public WxUserInfoModel getUserInfo_userinfo(String code) {
//		WxUserInfoModel userInfo = new WxUserInfoModel();
//		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Config.appIdBusiness + "&secret=" + Config.appSecretBusiness + "&code=" + code + "&grant_type=authorization_code";
//		String openId = "";
//		String acceccToken = "";
//
//		try {
//			JSONObject json = NetUtil.getJson(url);
//			logger.info(""+json);
//			openId = json.getString("openid");
//
//			acceccToken = json.getString("access_token");
//
//			String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + acceccToken + "&openid=" + openId + "&lang=zh_CN";
//			JSONObject jsonUserInfo = NetUtil.getJson(userInfoUrl, "utf-8");
////
//			logger.info(""+jsonUserInfo);
//
//			userInfo.setOpenId(openId);
//			if(jsonUserInfo.has("unionid")){
//				userInfo.setUnionid(jsonUserInfo.getString("unionid"));
//			}
//			if(jsonUserInfo.has("headimgurl")) {
//			userInfo.setHeadImgUrl(jsonUserInfo.getString("headimgurl"));
//			}
//			if(jsonUserInfo.has("nickname")) {
//				userInfo.setNickName(jsonUserInfo.getString("nickname"));
//			}
//			if(jsonUserInfo.has("sex")) {
//				userInfo.setSex(jsonUserInfo.getInt("sex"));
//			}
//			if(jsonUserInfo.has("country")) {
//				userInfo.setCountry(jsonUserInfo.getString("country"));
//			}
//			if(jsonUserInfo.has("province")) {
//				userInfo.setProvince(jsonUserInfo.getString("province"));
//			}
//			if(jsonUserInfo.has("city")) {
//				userInfo.setCity(jsonUserInfo.getString("city"));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			userInfo = null;
//			logger.error("e",e);
//		}
//		return userInfo;
//	}
//
//	@RequestMapping(value = "/pay_weixin", method = RequestMethod.GET)
//	public ModelAndView pay_weixin(HttpServletRequest request,
//		ModelAndView modelAndView,String o2oToken, String t0, String amount) {
//		logger.info("o2oToken = "+o2oToken+", t0="+t0+",amount="+amount);
//		 if (!is_weixin(request)) {// 不是微信浏览器
//	    	  modelAndView.setViewName("redirect:/pay/no_supported.do");
//	    	  return modelAndView;
//	      }
//
//		SessionService sessionService=(SessionService) ConsumerFactory.getBean("sessionService");
//		O2OSession o2oSession=sessionService.getO2OSessionByToken(o2oToken);
//
//		logger.info("o2oToken = "+o2oToken);
//
//
//		Object o=o2oSession.getAttribute("weixinpay_merchantId");
//		String merchantId ="";
//		if(o!=null)
//		{
//			merchantId = (String) o;
//		}
//		//String merchantId = (String) o2oSession.getAttribute("weixinpay_merchantId");// (String)getValueFromSession(request, "weixinpay_merchantId");
//
//		logger.info("merchantId = "+merchantId);
//		ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//		logger.info(""+shop);
//		if(shop == null) {
//			logger.info("can not find shop ,auto create");
//			shop=serviceFacade.createShop(merchantId, null);
//			//modelAndView.setViewName("redirect:http://www.yeahka.com");
//	    	//  return modelAndView;
//		}
//		if(StringUtil.isEmpty(shop.getFSmallImg())) {
//			shop.setFSmallImg("http://pic1.yeahka.com/img/pay/w_default_store.png");
//		}else {
//			shop.setFSmallImg(Config.YEAHKA_IMG_URL + shop.getFSmallImg());
//		}
//		modelAndView.addObject("merchantId", merchantId);
//		modelAndView.addObject("t0", t0);
//		modelAndView.addObject("amount", amount);
//		modelAndView.addObject("shop", shop);
//		modelAndView.setViewName("/pay/pay_weixin");
//
//	   	return modelAndView;
//
//	}
//
//	/**
//	 * 评价打星
//	 * @param request
//	 * @param modelAndView
//	 * @return
//	 */
//	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
//	public ModelAndView recommend(HttpServletRequest request,
//		ModelAndView modelAndView) {
//		try
//		{
//			 if (!is_weixin(request)) {// 不是微信浏览器
//				  modelAndView.setViewName("/pay/no_supported");
//		    	  return modelAndView;
//		      }
//
//			String merchantId = (String)getValueFromSession(request, "weixinpay_merchantId");
//			String orderId = (String)getValueFromSession(request,"weixinpay_leshua_order_id");
//
//			if(StringUtil.isEmpty(orderId)) {
//				orderId = request.getParameter("orderId");
//			}
//			boolean f = getPayService().flagPointsByOrderId(orderId+"_", 2);
//			if(f) {
//				String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;
//				modelAndView.addObject("mallIndex",webUrl+"/lbmall/home/index.do");
//				modelAndView.setViewName("/pay/recommend_lottery");
//			   	return modelAndView;
//			}
//			 if(StringUtil.isEmpty(orderId) || StringUtil.isEmpty(merchantId)) {
//				 logger.info("recommend orderId merchantId -> getSessionIdFail");
//				 modelAndView.setViewName("redirect:http://www.yeahka.com");
//				 return modelAndView;
//			 }
//
//			ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//			if(shop == null) {
//				logger.info("recommend shop -> getSessionIdFail");
//				modelAndView.setViewName("redirect:http://www.yeahka.com");
//				 return modelAndView;
//			}
//			if(StringUtil.isEmpty(shop.getFSmallImg())) {
//				shop.setFSmallImg("http://pic1.yeahka.com/img/pay/w_default_store.png");
//			}else {
//				shop.setFSmallImg(Config.YEAHKA_IMG_URL + shop.getFSmallImg());
//			}
//
//
//			modelAndView.addObject("merchantId", merchantId);
//			modelAndView.addObject("shop", shop);
//			modelAndView.setViewName("/pay/recommend");
//
//		   	return modelAndView;
//		}
//		catch(Exception e)
//		{
//			logger.info(e.getMessage());
//			e.printStackTrace();
//			throw e;
//		}
//
//	}
//
//	/**支付成功，微信会回调，在公众号配置这个地址
//	 * @param request
//	 * @param response
//	 * @throws DocumentException
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/wxNotify", method = RequestMethod.POST)
//	public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws DocumentException, IOException {
//		Map<String, String> mapkey = NetUtil.parseXml(request);
//		logger.info("消息解析完毕：map:"+mapkey);
//		String return_code = mapkey.get("return_code");
//		if(return_code.toUpperCase().equals("SUCCESS")) {
//
//			try {
//				String orderId = mapkey.get("out_trade_no");
//				String merchantId = getPayService().queryMerchantByOrderId(orderId);
//				String openId = mapkey.get("openid");
//				String amo = mapkey.get("total_fee");
//				String timeEnd = DateUtil.formatTime(mapkey.get("time_end"));
//				if(StringUtil.isEmpty(merchantId)) {
//					return;
//				}
//				ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//				boolean f = getPayService().flagPointsByOrderId(orderId,1);
//				if(f) {
//					 logger.info("查询有已经购买成功并送积分");
//					 response.getWriter().write(NetUtil.setXMLWx("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调了
//					 return;
//				}
//				Map<String,Object> map = getPayService().queryUnionId(openId,1);
//				logger.info("map = "+map);
//				if(!map.get("result").toString().equals("0")) {
//					 logger.info("查询unionid出错");
//					 response.getWriter().write(NetUtil.setXMLWx("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调了
//					 return;
//				}
//
//				Map<String,String> loginMap = getPayService().loginLeshua(openId,map.get("unionid").toString(),1);
//				logger.info("loginMap = "+loginMap);
//
//				//判断该商户是否支付成功
//				Map<String, String> orderMap = getPayService().queryOrder(orderId, merchantId);
//				logger.info("orderMap = "+orderMap);
//				if(orderMap.get("root.result").equals("0")) {
//
//					//增加积分
//					String uid = loginMap.get("root.uid"); //前期购买不赠送积分
////					int type = 6;//购买商品送积分
////					String desc = "微信支付购买商品送积分_H5";
////					int amount = Integer.valueOf(amo)/100;
////					if(amount > 0) {
////						Map<String, String> mallMap = getPayService().pointsmall(uid,amount,type,desc,orderId);
////						log.info("积分amount = "+amount);
////					}
////					getPayService().addPurchaseScore(uid,orderId,merchantId,Integer.valueOf(amo),-1,"",amount);
//
//					//公众号推送模板信息
//					SignService signService = (SignService)ConsumerFactory.getBean("signService");
//					long balance = signService.getBalanceByUid(Integer.valueOf(uid));
//					String toUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/o2o/pay/pushIndex.do?merchantId="+merchantId+"&orderId="+orderId;
//					getPayService().pushWxTemplateMessage(amo,orderId,openId,balance,timeEnd,toUrl,shop.getFShopName());
//
//					//商务版app推送信息
//					if((shop.getFBitFlag()&2)!=2) { //开通消息语音提示才发送
//					PushService pushService = (PushService) ConsumerFactory.getBean("pushService");
//					pushService.pushMsgWxPay(merchantId, "收款", "您收到一笔微信支付收款 "+Integer.valueOf(amo)/100.0+"元",amo);
//					}
//
//
//					//返回成功信息给微信
//					 response.getWriter().write(NetUtil.setXMLWx("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调了
//			            System.out.println("-------------"+NetUtil.setXMLWx("SUCCESS", ""));
//
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//	/**
//	 * 不支持非微信浏览器
//	 * @param request
//	 * @param modelAndView
//	 * @return
//	 */
//	@RequestMapping(value = "/no_supported", method = RequestMethod.GET)
//	public ModelAndView no_supported(HttpServletRequest request,
//		ModelAndView modelAndView) {
//
//		modelAndView.setViewName("/pay/no_supported");
//
//	   	return modelAndView;
//
//	}
//
//	@RequestMapping(value = "/recommend_lottery", method = RequestMethod.GET)
//	public ModelAndView recommend_lottery(HttpServletRequest request,
//		ModelAndView modelAndView) {
//		 if (!is_weixin(request)) {// 不是微信浏览器
//	    	  modelAndView.setViewName("redirect:/pay/no_supported.do");
//	    	  return modelAndView;
//	      }
//
//		modelAndView.setViewName("/pay/recommend_lottery");
//
//	   	return modelAndView;
//
//	}
//
//	@RequestMapping(value = "/push_test", method = RequestMethod.GET)
//	public String recommend_lottery_warning(HttpServletRequest request,
//		ModelAndView modelAndView,String merchantId,String amount) {
//		PushService pushService = (PushService) ConsumerFactory.getBean("pushService");
//		pushService.pushMsgWxPay(merchantId, "支付成功", "支付成功content",amount);
//
//		Map<String,Object> hello = new HashMap<String, Object>();
//		hello.put("result", "ok");
//	   	return JsonUtil.toJson(hello);
//
//	}
//	@RequestMapping(value = "/sned_test", method = RequestMethod.GET)
//	public String sned_test(HttpServletRequest request,
//		ModelAndView modelAndView) {
//		String merchantId = "0100105060";
//		long balance = 123L;
//		String toUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/o2o/pay/pay_weixin_userinfo.do?merchantId="+merchantId;
//		getPayService().pushWxTemplateMessage("141454","454645152","o8uJ6uNigBrCy3B0Y33OGr1wH6zc",balance,"2015-12-21 00:00:51",toUrl,"测试商店");
//		Map<String,Object> hello = new HashMap<String, Object>();
//		hello.put("result", "ok");
//	   	return JsonUtil.toJson(hello);
//
//	}
//
//	@RequestMapping(value = "/get_wxpay_data", method = RequestMethod.POST)
//	@ResponseBody
//	public String getWxPayData(HttpServletRequest request,HttpServletResponse response, Double amount, String t0)
//	{
//		Map<String, Object> map = new HashMap<String, Object>();
//		 if (!is_weixin(request)) {// 不是微信浏览器
//			 logger.info("不是微信浏览器");
//			 map.put("result", "1");
//			 map.put("err_msg", "请使用微信浏览器");
//			 return JsonUtil.toJson(map);
//	      }
//		 if (amount.toString().split("\\.")[1].length()>2 || amount <=0.0 )
//		 {
//			 logger.info("请输入两位小数:"+amount);
//			 map.put("result", "1");
//			 map.put("err_msg", "请输入两位小数是两位小数");
//			 return JsonUtil.toJson(map);
//		 }
//		 logger.info("t0=="+t0);
//		String result = "";
//		try
//		{
//			O2OSession o2oSession=getOrCreateO2OSession(request, response);
//			o2oSession.setAttribute("weixinpay_amount", amount);
//			saveO2OSession(o2oSession);
//			logger.info(""+o2oSession);
//			amount = amount*100;
//			Long amountLong = amount.longValue();
//			LinkedHashMap userinfo = (LinkedHashMap)getValueFromSession(request,"weixinpay_userinfo");
//			String merchantId = (String)getValueFromSession(request, "weixinpay_merchantId");
//			String username = (String)getValueFromSession(request, "weixinpay_username");
//			if(userinfo==null) {
//				userinfo = new LinkedHashMap();
//			}
//			logger.info("userinfo = "+userinfo.get("openId"));
//			logger.info("merchantId = "+merchantId);
////			userinfo.setOpenId("o8uJ6uNigBrCy3B0Y33OGr1wH6zc");//Jetman测试
//
//			String clientAddr = InternetProtocolUtil.getRemoteAddr(request);
//			logger.info("clientAddr===="+clientAddr);
//			Map<String, Object> mapData = getPayService().createWxPayData(merchantId,username, amountLong,
//					userinfo.get("openId").toString(), t0, clientAddr);
//			logger.info(""+mapData);
//			if(mapData.get("result").toString().equals("0")) {
//			result = mapData.get("leshua_order_id").toString() + "|" + mapData.get("weixin_pay_data").toString();
//			logger.info(result);
//			String orderId = mapData.get("leshua_order_id").toString();
//			o2oSession.setAttribute("weixinpay_leshua_order_id", orderId);
//			saveO2OSession(o2oSession);
//			}else {
//				result = "0|" + mapData.get("err_msg").toString();
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			result = "0|获取openId失败";
//			logger.error("e", e);
//		}
//		return result;
//	}
//
//	@RequestMapping(value = "/merchant_score", method = RequestMethod.GET)
//	public ModelAndView merchant_score(HttpServletRequest request, Integer score , String remark,ModelAndView modelAndView)
//	{
//		//Map<String, Object> map = new HashMap<String, Object>();
//		 if (!is_weixin(request)) {// 不是微信浏览器
//			 logger.info("不是微信浏览器");
//			 modelAndView.setViewName("redirect:/pay/no_supported.do");
//	    	  return modelAndView;
//	      }
//
//		 if(score==null || score<1 || score>5) {
//			 logger.info("评分为"+score+" 出错");
//			 logger.info("merchant_score -> getSessionIdFail");
//			 modelAndView.setViewName("redirect:http://www.yeahka.com");
//	    	  return modelAndView;
//		 }
//		 String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;
//		 if(remark!=null && remark.length()>500) {
//			 remark = remark.substring(0, 500);
//		 }
//		 int type = 7;//评价商品送积分
//		 String desc = "商家点评赠送";
//
//		 String uid = (String)getValueFromSession(request, "weixinpay_uid");
//		 String orderId = (String)getValueFromSession(request, "weixinpay_leshua_order_id");
//		 String merchantId = (String)getValueFromSession(request, "weixinpay_merchantId");
//		 Double businessAmount = (Double)getValueFromSession(request, "weixinpay_amount");
//
//		 if(StringUtil.isEmpty(orderId)) {
//			 logger.info("该用户不存在订单");
//			 logger.info("merchant_score orderId -> getSessionIdFail");
//			 modelAndView.setViewName("redirect:http://www.yeahka.com");
//	    	  return modelAndView;
//		 }
//		 orderId = orderId + "_";  //不能有相同的订单号，加个下划线标志评价订单号
//		 int amount = 10; //评价赠送10积分
//		 Map<String, String> mallMap = getPayService().pointsmall(uid,amount,type,desc,orderId);
//		 logger.info(""+mallMap);
//			if(!mallMap.get("root.result").equals("0")) {
//				modelAndView.addObject("mallIndex",webUrl+"/lbmall/home/index.do");
//				modelAndView.setViewName("/pay/recommend_lottery_warning");
//			   	return modelAndView;
//			}else {
//				try {
//					businessAmount = businessAmount*100;
//					int amountint = businessAmount.intValue();
//					getPayService().addPurchaseScore(uid,orderId,merchantId,amountint,score,remark,amount);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			modelAndView.addObject("mallIndex",webUrl+"/lbmall/home/index.do");
//			modelAndView.setViewName("/pay/recommend_lottery");
//		   	return modelAndView;
//	}
//
//	/**
//	 * 非屏幕适配的二维码
//	 * @param request
//	 * @param modelAndView
//	 * @param merchantId
//	 * @return
//	 */
//	@RequestMapping(value = "/wxQrCode", method = RequestMethod.GET)
//	public ModelAndView wxQrCode(HttpServletRequest request,
//		ModelAndView modelAndView,String merchantId) {
//		ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//		//商户名
//		String username = request.getParameter("username");
//		String sessionId = request.getParameter("session_id");
//		String qrUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"/o2o/pay/pay_weixin_userinfo.do?merchantId="+merchantId+"&username="+username;
//		modelAndView.addObject("merchantId", merchantId);
//		modelAndView.addObject("username", username);
//		modelAndView.addObject("qrUrl", qrUrl);
//		modelAndView.addObject("shop", shop);
//		modelAndView.addObject("session_id", sessionId);
//		modelAndView.setViewName("/pay/wxQrCode");
//
//	   	return modelAndView;
//
//	}
//
//	/**
//	 * 屏幕适配版本的二维码
//	 * @param request
//	 * @param modelAndView
//	 * @param merchantId
//	 * @return
//	 */
//	@RequestMapping(value = "/adapteScreenWxQrCode", method = RequestMethod.GET)
//	public ModelAndView adapteScreenWxQrCode(HttpServletRequest request,
//		ModelAndView modelAndView,String merchantId) {
//		ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//		//商户名
//		String username = request.getParameter("username");
//		String qrUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +"/o2o/pay/pay_weixin_userinfo.do?merchantId="+merchantId+"&username="+username;
//		modelAndView.addObject("merchantId", merchantId);
//		modelAndView.addObject("username", username);
//		modelAndView.addObject("qrUrl", qrUrl);
//		modelAndView.addObject("shop", shop);
//		modelAndView.setViewName("/pay/adapteScreenWxQrCode");
//
//	   	return modelAndView;
//
//	}
//
//	@RequestMapping(value = "/pushIndex", method = RequestMethod.GET)
//	public ModelAndView pushIndex(HttpServletRequest request,HttpServletResponse response,
//		ModelAndView modelAndView,String orderId,String merchantId) throws UnsupportedEncodingException {
//		   if (!is_weixin(request)) {// 不是微信浏览器
//		    	  modelAndView.setViewName("redirect:/pay/no_supported.do");
//		    	  return modelAndView;
//		      }
//
//		Map<String, Object> omap = getPayService().queryOrderByOrderId(orderId);
//		if(omap==null || !omap.get("F_merchant_id").toString().equals(merchantId)) {
//			logger.info("pushIndex -> getSessionIdFail");
//			  modelAndView.setViewName("redirect:http://www.yeahka.com");
//	    	  return modelAndView;
//		}
//		String amount = omap.get("F_amount").toString();
//		Double amo = Integer.valueOf(amount)/100.0;
//
//		O2OSession o2oSession=getOrCreateO2OSession(request, response);
//		String o2oToken = o2oSession.getToken();
//
//		o2oSession.setAttribute("weixinpay_leshua_order_id", orderId);
//		o2oSession.setAttribute("weixinpay_amount", amo);
//		saveO2OSession(o2oSession);
//
//		String callback = "/pay/recommend.do";
//		String webUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/o2o/pay/getOpenId.do?callback="+callback+"&merchantId="+merchantId+"&o2oToken="
//		+o2oToken+"&orderId="+orderId+"&amount="+amo;
//		String url = java.net.URLEncoder.encode(webUrl, "utf-8");
//		logger.info(url);
//		modelAndView.setViewName("redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Config.appIdBusiness+"&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect");
//
//	   	return modelAndView;
//
//	}
//
//	@RequestMapping(value = "/uploadHead", method = RequestMethod.GET)
//	@ResponseBody
//	public String uploadHead(HttpServletRequest request, String head,String merchantId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//			if(head.indexOf("/") == -1) {
//				//删除原来图片
//				O2OFile of = new O2OFile();
//				int dRs = of.deleteFile(shop.getFSmallImg(), 1);
//				logger.info(""+dRs);
//			}
//
//			int result = getShopService().uploadHeadImg(shop.getFMerchantUid(), head);
//			map.put("headUrl", Config.YEAHKA_IMG_URL+head);
//			map.put("error_code", "0");
//		}catch(Exception e) {
//			e.printStackTrace();
//			map.put("error_code", "-1");
//			logger.error("e",e);
//		}
//		return JsonUtil.toJson(map);
//	}
//
//	@RequestMapping(value = "/queryBitFlag", method = RequestMethod.GET)
//	@ResponseBody
//	public String queryBitFlag(HttpServletRequest request,String merchantId) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			if(StringUtils.isEmpty(merchantId))
//			{
//				merchantId="";
//			}
//			logger.info("merchantId={}",merchantId);
//			ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//			if(shop==null)
//			{
//				logger.info("Can not found shop by merchantId={}",merchantId);
//				map.put("voice", 0);
//				map.put("message",0);
//				map.put("error_code", "0");
//			}
//			else
//			{
//				//0表示默认开通
//				map.put("voice", (shop.getFBitFlag()&1)==1?0:1);
//				map.put("message", (shop.getFBitFlag()&2)!=2?1:0);
//				map.put("error_code", "0");
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//			map.put("error_code", "-1");
//			logger.error("e",e);
//		}
//		return JsonUtil.toJson(map);
//	}
//
//	@RequestMapping(value = "/updateBitFlag", method = RequestMethod.GET)
//	@ResponseBody
//	public String updateBitFlag(HttpServletRequest request,String merchantId,Integer type,Integer isDone) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		try {
//			boolean flag = false;
//			ShopModel shop = getShopService().getShopModelByMerchantId(merchantId);
//			logger.info(""+shop.getFBitFlag());
//			logger.info(""+type);
//			logger.info(""+isDone);
//			isDone = isDone==1?0:1;
//			logger.info("0为默认开通 isDone变化后 = "+isDone);
//			if(type >= 0 && type <2) {
//			Integer bitFlag = StringUtil.setState(shop.getFBitFlag(), type, isDone);
//			flag = getShopService().updateShopBitFlag(merchantId,bitFlag);
//			}
//			if(flag == true)
//			map.put("error_code", "0");
//			else
//				map.put("error_code", "-1");
//		}catch(Exception e) {
//			e.printStackTrace();
//			map.put("error_code", "-1");
//			logger.error("e",e);
//		}
//		return JsonUtil.toJson(map);
//	}
//
//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public ModelAndView test(HttpServletRequest request,
//		ModelAndView modelAndView,String view_name) {
//		ShopModel shop = getShopService().getShopModelByMerchantId("0000000018");
//		shop.setFSmallImg(Config.YEAHKA_IMG_URL + shop.getFSmallImg());
//		modelAndView.addObject("shop", shop);
//		modelAndView.setViewName("/pay/"+view_name);
//
//	   	return modelAndView;
//
//	}
//
//	@RequestMapping(value="/unifiedpay",method=RequestMethod.GET)
//	public ModelAndView unifiedpay(HttpServletRequest request,ModelAndView modelAndView) throws UnsupportedEncodingException {
//		String wxPayData = request.getParameter("wxPayData");
//		if (StringUtil.isEmpty(wxPayData)) {
//			modelAndView.addObject("errmsg", "缺少支付信息");
//			modelAndView.setViewName("/pay/unifiedpayfail");
//			return modelAndView;
//		}
//
//		String callbackUrl = request.getParameter("callbackUrl");
//		if (StringUtil.isEmpty(callbackUrl)) {
//			modelAndView.addObject("errmsg", "缺少回调地址");
//			modelAndView.setViewName("/pay/unifiedpayfail");
//			return modelAndView;
//		}
//		modelAndView.addObject("callbackUrl", callbackUrl);
//		callbackUrl = URLDecoder.decode(callbackUrl,"utf-8");
//		//判断url是否带参数
//		if (callbackUrl.indexOf("?") > 0) {
//			modelAndView.addObject("urlHasParameter", 1);
//		}else {
//			modelAndView.addObject("urlHasParameter", 0);
//		}
//		if (StringUtil.isNotEmpty(request.getParameter("amount"))) {
//			modelAndView.addObject("amount", request.getParameter("amount"));
//		}
//		modelAndView.addObject("wxPayData",URLDecoder.decode(wxPayData,"utf-8"));
//		modelAndView.setViewName("/pay/unifiedpay");
//		return modelAndView;
//	}
//
//	@RequestMapping(value="/getMerchantPayURL",method=RequestMethod.GET)
//	@ResponseBody
//	public String getMerchantPayURL(HttpServletRequest request,HttpServletResponse response,String merchantId,String username)
//	{
//		HashMap<String, Object> result=new HashMap<String, Object>();
//		if(StringUtils.isEmpty(merchantId) || StringUtils.isEmpty(username))
//		{
//			result.put("code", -1);
//			result.put("message", "merchantId or username is error");
//			return JsonUtil.toJson(result);
//		}
//
//
//		ShopService shopService = (ShopService) ConsumerFactory.getBean("shopService");
//		ShopModel shop =shopService.getShopModelByMerchantId(merchantId);
//		if(shop==null)
//		{
//			result.put("code", -1);
//			result.put("message", "merchant is not exist");
//			return JsonUtil.toJson(result);
//		}
//
//		//MerchantService merchantService = (MerchantService) ConsumerFactory.getBean("merchantService");
//		//MerchantModel merchant = merchantService.getMerchentModelByMerchantId(merchantId);
//
//		result.put("code", 0);
//		result.put("message", "success");
//		result.put("url", String.format("http://pos.yeahka.com/o2o/pay/pay_weixin_userinfo.do?merchantId=%s&username=%s", merchantId,username));
//
//		return JsonUtil.toJson(result);
//
//		//return JSONObject.
//	}
//
//
//	public static void main(String[] args) throws IOException {
//		Double a = 0.01;
//		System.out.println(a.intValue());
//	}
//}
