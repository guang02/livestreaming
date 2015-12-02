package edu.sysu.netlab.livestreaming.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

/**
 * 该类主要功能为验证postde1Email数据的格式问题。<br>
 * 例如email格式是否正确，时间格式是否正确等等。当post数据错误时
 * 就立即给前端返回提示信息。
 * @author JoshuaShaw
 *
 */
public class EmailValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		validateEmail("email", "emailMsg", "Email格式有误！");		
	}

	@Override
	protected void handleError(Controller c) {
		ResponseJson rj = new ResponseJson().setCode(ResponseCode.Success);
		
		rj.setMessage( c.getAttr("emailMsg") );
		c.renderJson(rj.toString());	
	}

}
