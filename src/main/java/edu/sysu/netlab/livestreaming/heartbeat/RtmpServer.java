package edu.sysu.netlab.livestreaming.heartbeat;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.sysu.netlab.livestreaming.model.LiveRoom;

public class RtmpServer<K extends InetAddress, V> extends AbstractServer<K, V>{

	public static final int RTMP_PORT = 1935;
	
	public RtmpServer(String uri) throws Exception{
		super(uri);
	}
	
	public void print(){
		System.err.println("[");
		System.err.println("    ip: " + ip.getHostAddress());
		System.err.println("]");
	}

	@Override
	public int getPort() {
		return RTMP_PORT;
	}

}
