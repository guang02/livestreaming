package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.sysu.netlab.livestreaming.heartbeat.ServerDispatcher;

public class ServerDispatcherTest {

	@Test
	public void test() throws InterruptedException{
		ServerDispatcher sd = ServerDispatcher.self();
		Thread.sleep(3000);
		int count = sd.getAvailableServerNumber();
		System.out.println(count);
		assertEquals(1, count);
	}

}
