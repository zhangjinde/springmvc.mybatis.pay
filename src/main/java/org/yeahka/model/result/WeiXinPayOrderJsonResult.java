package org.yeahka.model.result;

public class WeiXinPayOrderJsonResult extends JsonResult {

	private String leshua_order_id;
	private String weixin_pay_data;

	public WeiXinPayOrderJsonResult(int code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	public void setLeshua_order_id(String leshua_order_id) {
		// TODO Auto-generated method stub
		this.leshua_order_id=leshua_order_id;
	}

	public void setWeixin_pay_data(String weixin_pay_data) {
		// TODO Auto-generated method stub
		this.weixin_pay_data=weixin_pay_data;
	}

	public String getLeshua_order_id() {
		return leshua_order_id;
	}

	public String getWeixin_pay_data() {
		return weixin_pay_data;
	}



}
