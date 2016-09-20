package org.yeahka.model.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json返回结果的公共属性
 * @author dax
 *
 */
public class MerchantNameJsonResult extends JsonResult {

	private String merchantName;

	public MerchantNameJsonResult(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	private final static Logger logger=LoggerFactory.getLogger(MerchantNameJsonResult.class);

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public static Logger getLogger() {
		return logger;
	}



}
