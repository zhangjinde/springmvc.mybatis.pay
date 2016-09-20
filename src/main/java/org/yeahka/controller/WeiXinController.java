package org.yeahka.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.yeahka.commons.utils.InternetProtocolUtil;
import org.yeahka.commons.utils.NetUtil;
import org.yeahka.config.Config;
import org.yeahka.model.result.JsonResult;
import org.yeahka.model.result.WeiXinPayOrderJsonResult;
import org.yeahka.service.PayService;

/**
 * 微信授权并获取用户信息相关接口
 *
 * @author dax
 *
 */
@Controller
public class WeiXinController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(WeiXinController.class);

	@Autowired
	private PayService payService;

	@RequestMapping(value = "/toWeiXinAuthorize")
	public ModelAndView toAuthorize(HttpServletRequest request, HttpServletResponse response, PayParams payParam) throws IOException {
		logger.info("toWeiXinAuthorize,payParam="+payParam.toQueryString());
		ModelAndView mv = new ModelAndView();
		try {
			if (StringUtils.isEmpty(payParam.getMerchantId())) {
				logger.info("merchantId can not null");
				mv.setViewName(redirectToErrorPage(getErrorPageUrl(), "merchantId can not null"));
				return mv;
			}

			String redirectUri = getAuthorizeRedirectUri(Config.getConfiguration().getString("weixin.callback.router.url"), payParam);

			String authorizeUrl = String
					.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=123&connect_redirect=1#wechat_redirect",
							Config.getConfiguration().getString("appId"), redirectUri);

			logger.info(String.format("authorizeUrl=%s", authorizeUrl));
			mv.setViewName("redirect:" + authorizeUrl);
			return mv;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			mv.setViewName(redirectToErrorPage(getErrorPageUrl(), e.getMessage()));
			return mv;
		}

	}

	private String getAuthorizeRedirectUri(String baseUrl, PayParams payParam) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String url = String.format("%s/getOpenId.do?%s", baseUrl+"/pay", payParam.toQueryString());
		logger.info("url=" + url);
		String callback = java.net.URLEncoder.encode(url, "utf-8");
		logger.info("encoded url=" + callback);
		return callback;
	}

	@RequestMapping(value = "/getOpenId")
	public ModelAndView getOpenId(HttpServletRequest request, HttpServletResponse response, PayParams payParam, String code) throws IOException {
		logger.info("getOpenId");
		ModelAndView mv=new ModelAndView();
		try {
			String openId = fetchOpenIdFromWeiXin(code);
			if (StringUtils.isEmpty(openId)) {
				logger.info("Can not get openId");
				mv.setViewName(redirectToErrorPage(getErrorPageUrl(), "Can not get openId"));
				return mv;
			}

			logger.info("openId=" + openId);

			mv.setViewName(getPayRedirectUrl(getPayPageUrl(), payParam, openId));
			return mv;

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			mv.setViewName(redirectToErrorPage(getErrorPageUrl(), e.getMessage()));
			return mv;
		}
	}

	private String getPayRedirectUrl(String baseUrl,PayParams payParam, String openId) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String url = String.format("redirect:%s?%s&openId=%s",
				baseUrl, payParam.toQueryString(),openId);
		logger.info("getPayRedirectUrl=" + url);
		return url;
	}

	public String fetchOpenIdFromWeiXin(String code) {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Config.getConfiguration().getString("appId") + "&secret="
				+ Config.getConfiguration().getString("appSecret") + "&code=" + code + "&grant_type=authorization_code";
		String openId = "";

		try {
			JSONObject json = NetUtil.getJson(url);
			logger.info("" + json);
			openId = json.getString("openid");
			return openId;
			// userInfo.setOpenId(openId);
			// if(json.has("unionid")){
			// userInfo.setUnionid(json.getString("unionid"));
			// }
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/createWeiXinOrder")
	@ResponseBody
	public String createWeiXinOrder(HttpServletRequest request, HttpServletResponse response, PayParams payParam,String openId) throws IOException {
		logger.info(String.format("createWeiXinOrder,payParam=%s,openId=%s",payParam.toQueryString(),openId));

		try {

			if (StringUtils.isEmpty(openId)) {
				logger.info("openId can not null");
				return new JsonResult(-1, "openId参数不能为null").toString();
			}

			String clientAddr = InternetProtocolUtil.getRemoteAddr(request);
			logger.info("test get clientAddr==" + clientAddr);

			Map<String, Object> mapData = payService.createWxPayData(payParam.getMerchantId(), payParam.getUsername(),((Float)(Float.parseFloat(payParam.getAmount())*100)).longValue(),
					openId, payParam.getT0(), clientAddr);

			/**
			 * {result=0, weixin_pay_data={"appId":"wx504536a84f74ba0b",
			 * "nonceStr":"b80839d5f28fda2d822b0e9b6a844188",
			 * "package":"prepay_id=wx201609151238477b80c917740933009154",
			 * "paySign":"CD9EF4FB7B5A781CD4CA3213C7B80EFF",
			 * "signType":"MD5","timeStamp":"1473914327"},
			 * leshua_order_id=1609151238472993}
			 */

			logger.info("" + mapData);
			if (mapData.get("result").toString().equals("0")) {
				WeiXinPayOrderJsonResult result = new WeiXinPayOrderJsonResult(0,"ok");
				result.setLeshua_order_id(mapData.get("leshua_order_id").toString());
				result.setWeixin_pay_data(mapData.get("weixin_pay_data").toString());
				logger.info(result.toString());
				return result.toString();
			} else {
				return new JsonResult(-1, mapData.get("err_msg").toString()).toString();
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return new JsonResult(-1, e.getMessage()).toString();
		}
	}


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

}
