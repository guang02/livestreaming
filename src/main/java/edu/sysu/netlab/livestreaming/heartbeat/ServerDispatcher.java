package edu.sysu.netlab.livestreaming.heartbeat;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.jfinal.kit.PropKit;

import edu.sysu.netlab.livestreaming.model.LiveRoom;

public class ServerDispatcher {

	private static ServerDispatcher self = null;
	
	static {
		self = new ServerDispatcher();
	}
	
	private ScheduledExecutorService threadPools = null;
	private List<RtmpServer<InetAddress, LiveRoom>> rtmpServersList = null;
	private List<RtmpServer<InetAddress, LiveRoom>> availableRtmpServersList = null;
	private volatile int availableRtmpServersNumber = 0;
	
	
	private ServerDispatcher() {
		threadPools = Executors.newScheduledThreadPool(5);
		rtmpServersList = new ArrayList<>();
		availableRtmpServersList = rtmpServersList;
		loadRtmpServers();
		availableRtmpServersNumber = availableRtmpServersList.size();
		
		threadPools.scheduleAtFixedRate(
				new HeartBeatTask(), 0, 10, TimeUnit.HOURS);
				
		
	}
	
	private void loadRtmpServers(){
		String[] uris = PropKit.use("config.properties").get("rtmp").split(",");
		for(String uri:uris) {
			try {
				RtmpServer<InetAddress, LiveRoom> rtmpServer = new RtmpServer<>(uri);
				rtmpServersList.add(rtmpServer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ServerDispatcher self() {
		try{
			Objects.requireNonNull(self);
		}catch(NullPointerException e) {
			self = new ServerDispatcher();
		}
		return self;
	}
	
	public int getAvailableServerNumber() {
		return availableRtmpServersNumber;
	}
	
	public RtmpServer<InetAddress, LiveRoom> requestForServer(InetAddress ip) throws NotAvailableServerException{
		if(getAvailableServerNumber()<=0){
			throw new NotAvailableServerException();
		}		
		return availableRtmpServersList.get( 0 );
	}
	
	
	class HeartBeatTask implements Runnable{
		@Override
		public void run() {
			synchronized (availableRtmpServersList) {
				Socket testSocket = null;
				for( RtmpServer<InetAddress, LiveRoom> server : rtmpServersList ) {
					try{	
						testSocket = new Socket(server.getServerIp(), server.getPort());	
						testSocket.close();
						server.setAlive(true);
						availableRtmpServersNumber +=1;

					} catch(Exception e) {	
						server.setAlive(false);
						availableRtmpServersNumber -=1;
						e.printStackTrace();
					} finally {
						server.print();
					}
				}						
				availableRtmpServersList = 
						Collections.synchronizedList(
								rtmpServersList.stream()
						                      .filter(e->e.isAlive())
						                      .collect(Collectors.toList())
						                           );
			}
			
		}		
	}
	
	public static void main(String[] args) {
		ServerDispatcher sd = ServerDispatcher.self();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println( sd.getAvailableServerNumber() );
	}
	
}
