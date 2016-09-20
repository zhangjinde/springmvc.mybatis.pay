package org.yeahka.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 针对微信和支付宝扫码支付时，需要传递商户校验key和t0参数而单独定义的一个实体类
 *
 * @author scofreld
 *
 */
public class Merchant {

	/**
	 * /** <result column="F_bind_merchant_key" property="merchantKey" />
	 * <result column="F_merchant_flag" property="merchantFlag" /> <result
	 * column="F_merchant_id" property="merchantId" /> <result
	 * column="F_merchant_name" property="merchantName" />
	 */

	private String merchantId;
	private String merchantName;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/** 商户key */
	private String merchantKey;

	/** t0标识,值为1表示t0,其他均为t1 */
	private int merchantFlag;

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public int getMerchantFlag() {
		return merchantFlag;
	}

	public void setMerchantFlag(int merchantFlag) {
		this.merchantFlag = merchantFlag;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
