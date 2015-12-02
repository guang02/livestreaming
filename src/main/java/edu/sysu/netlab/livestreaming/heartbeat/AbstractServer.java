package edu.sysu.netlab.livestreaming.heartbeat;

import java.net.InetAddress;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServer<K extends InetAddress, V> implements Server<K, V> {

	protected InetAddress ip;
	protected URI uri;
	protected Map<K, V> clientMap;
	protected boolean alive;
	protected int clientNumber;
	
	public AbstractServer(String uri) throws Exception{
		this.uri = URI.create(uri);
		ip = InetAddress.getByName(this.uri.getHost());
		clientMap = Collections.synchronizedMap( new HashMap<>() );
	}
	
	@Override
	public InetAddress getServerIp() {
		return ip;
	}

	@Override
	public URI getURI() {
		return uri;
	}

	@Override
	public void setAlive(boolean alive) {
		this.alive = alive; 
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public int getClientNumber() {
		return clientMap.size();
	}

	@Override
	public void addClient(K k, V v) {
		clientMap.put(k, v);		
	}
	
	@Override
	public Map<K, V> getClientMap() {
		return clientMap;
	}
	
	@Override
	public abstract int getPort();

}
