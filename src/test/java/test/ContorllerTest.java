package test;

import org.junit.Test;

import com.jfinal.ext.test.ControllerTestCase;
import static org.junit.Assert.assertEquals;

import edu.sysu.netlab.livestreaming.config.JFinalProjectConfig;

public class ContorllerTest extends ControllerTestCase<JFinalProjectConfig> {
	
	public void doTest(){
		String result = use("/").invoke();
		assertEquals(result, "Hello world!");
	}
	@Test
	public void registerFailed() {
		ControllerTestCase<JFinalProjectConfig> register = use("/user/register?email=455532734@qq.com&password=123&nickName=haha");
		
		String result = register.post("?email=455532734@qq.com&password=123&nickName=haha").invoke();
		System.out.println(result);
		assertEquals(result, "{\"code\":600,\"data\":{},\"message\":\"Email已经被注册！\"}");
	}
	

	
}
