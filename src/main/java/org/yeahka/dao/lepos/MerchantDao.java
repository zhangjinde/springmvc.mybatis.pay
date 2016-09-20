package org.yeahka.dao.lepos;

import java.util.Map;

import org.yeahka.model.Merchant;

public interface MerchantDao {

	Merchant getMerchantById(Map<String,Object> map);

}
