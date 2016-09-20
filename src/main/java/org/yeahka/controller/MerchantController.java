package org.yeahka.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yeahka.commons.utils.JsonUtil;
import org.yeahka.commons.utils.MD5;
import org.yeahka.model.Merchant;
import org.yeahka.model.result.JsonResult;
import org.yeahka.model.result.MerchantNameJsonResult;
import org.yeahka.service.PayService;

/**
 * 微信授权并获取用户信息相关接口
 *
 * @author dax
 *
 */
@Controller
public class MerchantController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(MerchantController.class);

	/** 商户支付时进行签名验证的key */
	private final static String SIGN_KEY = "MNBVCXZQWEEASLKDFJKLAJFQWNE23IFK";

	@Autowired
	private PayService payService;

	/**
	 * 获取商户名称接口
	 *
	 * @param request
	 * @param response
	 * @param payParam
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getMerchantName")
	@ResponseBody
	public String getMerchantName(HttpServletRequest request, HttpServletResponse response, PayParams payParam) throws IOException {

		logger.info("getMerchantName payParams="+payParam.toQueryString());
		try
		{
			Merchant merchant = payService.getMerchantById(payParam.getMerchantId());

			MerchantNameJsonResult result = new MerchantNameJsonResult(0, "ok");
			result.setMerchantName(merchant.getMerchantName());
			return result.toString();
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			e.printStackTrace();
			return new JsonResult(-1, e.getMessage()).toString();
		}
	}

	/**
	 * 创建乐刷后台支付订单
	 *
	 * @param request
	 * @param response
	 * @param payParam
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/createPayOrder")
	public ModelAndView createPayOrder(HttpServletRequest request, HttpServletResponse response, PayParams payParam) throws IOException {

		logger.info("createPayOrder payParams="+payParam.toQueryString());
		ModelAndView mv = new ModelAndView();

		if (is_weixin(request)) {// 是微信浏览器

			mv.setViewName(toCreateWeiXinOrder(getBaseUrl(request),payParam));
			return mv;
		}
		else if(is_alipay(request))
		{
			mv.setViewName(toCreateAlipayOrder(getBaseUrl(request),payParam));
			return mv;
		}
		else
		{
			logger.info("不是微信浏览器");
			mv.setViewName(redirectToErrorPage(getErrorPageUrl(), "请使用微信浏览器"));
			return mv;
		}

	}

	private String toCreateAlipayOrder(String baseUrl, PayParams payParam) {
		// TODO Auto-generated method stub
		return null;
	}

	private String toCreateWeiXinOrder(String baseUrl, PayParams payParam) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String url = String.format("redirect:%s/toWeiXinAuthorize.do?%s", baseUrl,payParam.toQueryString());
		logger.info("toCreateWeiXinOrder"+url);
		return url;
	}


	/**
	* 提供给app调用，用来获取带签名的支付请求地址
	* @param request
	* @param response
	* @param merchantId 商户ID
	* @param username 用户名
	* @return
	* @throws UnsupportedEncodingException
	*/
	@RequestMapping(value = "/merchant_shop_url", method = RequestMethod.GET)
	@ResponseBody
	public String getMerchantShopUrl(HttpServletRequest request,HttpServletResponse response,
		String merchantId, String username) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "-1");
		map.put("url","");
		if (StringUtils.isEmpty(merchantId) || "null".equals(merchantId)) {
			map.put("message", "merchantId不能为空");
			return JsonUtil.toJson(map);
		}
		if (StringUtils.isEmpty(username) || "null".equals(username)) {
			map.put("message", "username不能为空");
			return JsonUtil.toJson(map);
		}
		String sign = MD5.md5(SIGN_KEY + merchantId).toUpperCase();
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ "/o2o/pay/merchant_shop.do?merchantId="+merchantId+"&username="+username+"&sign="+sign;
		map.put("code", "0");
		map.put("message", "ok");
		map.put("url",url);
		return JsonUtil.toJson(map);
	}

//	/**
//	* 新增的带签名认证的支付请求接口
//	* @param request
//	* @param response
//	* @param modelAndView
//	* @param merchantId
//	* @param t0
//	* @param amount
//	* @param sign
//	* @return
//	* @throws UnsupportedEncodingException
//	*/
//	@RequestMapping(value = "/merchant_shop", method = RequestMethod.GET)
//	public ModelAndView merchant_shop(HttpServletRequest request,HttpServletResponse response,
//		ModelAndView modelAndView,String merchantId, String t0, String amount, String sign) throws UnsupportedEncodingException {
//		logger.info("merchant_shop--t0="+t0+",amount="+amount+",sign="+sign);
//		try
//		{
//			if(StringUtils.isEmpty(merchantId))
//			{
//				throw new InvalidParameterException("merchantId can not null");
//			}
//
//			String md5Sign = MD5.md5(SIGN_KEY + merchantId).toUpperCase();
//			if(!md5Sign.equals(sign)) {
//				throw new InvalidParameterException("sign error");
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
//			String o2oToken = "";
//	//		O2OSession o2oSession=getO2OSession(request, null);
//	//		if(o2oSession!=null)
//	//		{
//	//			o2oToken=o2oSession.getToken();
//	//		}
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
//	}


}
