package edu.sysu.netlab.livestreaming.interceptor;

import java.util.Objects;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;


/**
 * 登录拦截器<br>
 * 依据session中是否存在userId来判断<br>
 * <br>
 * @author JoshuaShaw
 * @version 0.1
 */
public class LoginInterceptor implements Interceptor {

	private Controller loginController = null;
	
	public LoginInterceptor() {}
	
	@Override
	public void intercept(Invocation inv) {
		loginController = inv.getController();
		try{
			Objects.requireNonNull( loginController.getSessionAttr("userId") );

			inv.invoke();
			
		} catch(NullPointerException e) {
			//e.printStackTrace();
			loginController.forwardAction("/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
