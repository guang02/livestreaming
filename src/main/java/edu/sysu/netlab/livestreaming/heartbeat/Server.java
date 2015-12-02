package edu.sysu.netlab.livestreaming.heartbeat;

import java.net.InetAddress;
import java.net.URI;
import java.util.Map;

public interface Server<K extends InetAddress, V> 
       extends Comparable<Server<K, V>>{
	/*
	private InetAddress ip = null;
	private String app = null;
	private String url = null;
	private boolean alive = false;
	private int clientCount = 0;
	private Map<String, LiveRoom> liveRoomsMap;
	public static final int RTMP_PORT = 1935;
	*/
	
	public InetAddress getServerIp();
	public URI getURI();
	public void setAlive(boolean alive);
	public boolean isAlive();
	public int getClientNumber();
	public int getPort();
	public void addClient(K k, V v);
	public Map<K, V> getClientMap();
	
	@Override
	default int compareTo(Server<K, V> o) {
		return this.getURI().compareTo(o.getURI());
	}
}
