package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

public class ResponseJsonTest {
	
	@Test
	public void test(){
		ResponseJson rj = new ResponseJson();
		System.out.println(rj.toString());
	}
}
