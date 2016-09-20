package org.yeahka.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yeahka.commons.utils.NetUtil;
import org.yeahka.commons.utils.SHA1Util;
import org.yeahka.commons.utils.TableName;
import org.yeahka.config.Config;
import org.yeahka.dao.lepos.MerchantDao;
import org.yeahka.model.Merchant;

//订单信息
@Service
public class PayServiceImpl implements PayService
{
//	static final Log logger = LogFactory.getLog(PayServiceImpl.class);
	private final static Logger logger=LoggerFactory.getLogger(PayServiceImpl.class);
	private final static String PAY_CGI = "http://mobilepos.yeahka.com/cgi-bin/";

	@Autowired
	private MerchantDao merchantDao;

	@Override
	public Map<String, Object> createWxPayData(String merchantId,String username, Long amount,
			String openId, String t0, String clientAddr) {
		logger.info("createWxPayData clientAddr==="+clientAddr);
		/**
		 * 因为要实现一个二维码对应t0和t1两种情况，所以不再使用传入的t0参数，改为通过商户ID去数据库里面取
		 * modify by scofreld at 2016-08-30
		 *
		 * 需要兼容回app传过来的t0参数，如果有传t0过来则使用传进来的，没有传则使用商户ID查询出来的
		 * modify by scofreld at 2016-09-06
		 */
		logger.info("app send t0==="+t0);
		if(StringUtils.isEmpty(t0) || "null".equals(t0)){
			t0 = String.valueOf(getMerchantFlag(merchantId));
			logger.info("t0 from db====="+t0);
		}

		if(StringUtils.isEmpty(username)) {
			username = "";
		}

		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer queryString = new StringBuffer();
		queryString.append("cmd=get_tdcode");
		queryString.append("&amount=" + amount);
		queryString.append("&client_addr=" + clientAddr);
		queryString.append("&merchant_id=" + merchantId);
		queryString.append("&openid=" + openId);
		queryString.append("&t0=" + t0);
		queryString.append("&user_name=" + username);
		//String LESHUACGIKEY = "23707829157576652437095531190535";
		//System.out.println(LESHUACGIKEY);
		logger.info("CALL SERVER签名原始串:" + new String(queryString));
		// 签名
		String sign = SHA1Util.encrypt(new String(queryString)).toUpperCase();
		try
		{
			queryString = new StringBuffer();
			queryString.append("cmd=get_tdcode");
			queryString.append("&amount=" + amount);
			queryString.append("&client_addr=" + clientAddr);
			queryString.append("&merchant_id=" + merchantId);
			queryString.append("&openid=" + openId);
			queryString.append("&t0=" + t0);
			queryString.append("&user_name=" + username);
			String url = String.format("%s?%s&sign=%s",Config.getConfiguration().getString("PAY_CGI") ,queryString,sign);
			logger.info("pay cgi="+url);
			Map<String, String> rs = NetUtil.getXml(url);
			logger.info("pay cgi result="+rs);
			map.put("result", rs);
			if (rs.get("root.result").equals("0"))
			{
				map.put("result", 0);
				String payData = rs.get("root.weixin_pay_data");
				logger.info("返回数据===" + payData);
				map.put("weixin_pay_data", rs.get("root.weixin_pay_data"));
				map.put("leshua_order_id", rs.get("root.leshua_order_id"));
			}else {
				map.put("result", -1);
				map.put("err_code", rs.get("root.internal_error_code"));
				map.put("err_msg", rs.get("root.internal_error_msg"));
			}
		}
		catch (Exception e)
		{
			map.put("result", -1);
			e.printStackTrace();
		}
		logger.info(""+map);
		return map;
	}


	/**
	 * 获取商户的t0标识，1为t0,非1则为t1
	 * @param merchantId
	 * @return
	 */
	private int getMerchantFlag(String merchantId){
		int result = 0;
		logger.info("getMerchantFlag");
		String sql = "select F_merchant_flag from "+TableName.getMerchantTable(merchantId)+" where F_merchant_id = ? limit 1";


		//int flag =  baseWebDaoSlave.executeQueryInteger(sql, merchantId);
		Merchant merchant =getMerchantById(merchantId);
		if(merchant==null)
		{
			logger.info("getMerchantFlag can not find merchant by id="+merchantId);
			return 0;
		}

		logger.info("flag=="+merchant.getMerchantFlag());
		if(chkState(merchant.getMerchantFlag(),9)){
			result = 1;
		}
		return result;
	}

	public static boolean chkState(int state, int bit) {
		String state_bit = Integer.toBinaryString(state);

		// 判断位数
		if (state_bit.length() >= bit) {
			// 把二进制串倒置 因为java的最低位在左边
			state_bit = new StringBuffer(state_bit).reverse().toString();
			if (state_bit.length() > bit && state_bit.charAt(bit) == '1') {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}


	@Override
	public Merchant getMerchantById(String merchantId) {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("merchantId", merchantId);
		map.put("dbAndTable", TableName.getMerchantTable(merchantId));

		return merchantDao.getMerchantById(map);
	}



}
