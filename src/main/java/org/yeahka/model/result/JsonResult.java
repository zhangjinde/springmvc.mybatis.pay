package org.yeahka.model.result;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json返回结果的公共属性
 * @author dax
 *
 */
public class JsonResult {

	private final static Logger logger=LoggerFactory.getLogger(JsonResult.class);

	private int code;
	private String message;

	/**
	 * 兼容老的app接口，值与code保持一致
	 */
	private int error_code;
	/**
	 * 兼容老的app接口，值与message保持一致
	 */
	private String error_msg;


	public int getError_code() {
		return error_code;
	}
	public String getError_msg() {
		return error_msg;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public JsonResult(int code, String message) {
		super();
		this.code = code;
		this.message = message;
		this.error_code=code;
		this.error_msg=message;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		return "";
	}

}
