package org.yeahka.service;

import java.util.Map;

import org.yeahka.model.Merchant;

public interface PayService {
	public Map<String, Object> createWxPayData(String merchantId,String username, Long amount,
			String openId, String t0, String clientAddr);

	public Merchant getMerchantById(String merchantId);
}
