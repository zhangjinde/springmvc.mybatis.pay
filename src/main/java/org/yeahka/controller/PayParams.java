package org.yeahka.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PayParams {

	private String merchantId;
	private String username;
	private String t0;
	private String amount;

	/**
	 * 商户id
	 */
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * 商户名称
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 是否t0,1为t0
	 * @return
	 */
	public String getT0() {
		return t0;
	}
	public void setT0(String t0) {
		this.t0 = t0;
	}
	/**
	 * 生成查询字符串
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Object toQueryString() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		/**
		 * String webUrl = baseUrl/getOpenId.do?callback="+callback+"&merchantId="+merchantId+"&username="+username
				+"&o2oToken="+o2oToken+"&t0="+t0+"&amount="+amount;
		 */
		StringBuilder sb=new StringBuilder();
		if(merchantId!=null)
		{
			sb.append("merchantId="+merchantId);
			sb.append("&");
		}

		if(username!=null)
		{
			sb.append("username="+URLEncoder.encode(username, "utf-8"));
			sb.append("&");
		}

		if(t0!=null)
		{
			sb.append("t0="+t0);
			sb.append("&");
		}

		if(amount!=null)
		{
			sb.append("amount="+amount);
			sb.append("&");
		}

		return sb.substring(0,sb.length()-1);

		//return String.format("merchantId=%s&username=%s&t0=%s&amount=%s", merchantId,URLEncoder.encode(username,  "utf-8"),t0,amount);
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}




}
