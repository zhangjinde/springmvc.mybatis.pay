

import org.junit.Test;
import org.yeahka.model.result.WeiXinPayOrderJsonResult;

public class WeiXinPayOrderJsonResultTest {

	@Test
	public void testToString() {
		WeiXinPayOrderJsonResult result=new WeiXinPayOrderJsonResult(0, "ok");
		result.setLeshua_order_id("leshua_order_id");
		result.setWeixin_pay_data("weixin_pay_data");
		System.out.println(result.toString());
	}

}
