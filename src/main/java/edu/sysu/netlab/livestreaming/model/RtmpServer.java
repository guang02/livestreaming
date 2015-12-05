package edu.sysu.netlab.livestreaming.model;

import java.util.List;
import java.util.Random;

import com.jfinal.plugin.activerecord.Model;

public class RtmpServer extends Model<RtmpServer> {
	public static RtmpServer dao = new RtmpServer(); 
	
	public static RtmpServer getRtmpServer() throws Exception{
		String sql = "select * from RtmpServer";
		List<RtmpServer> servers =  dao.find(sql);
		int index = new Random().nextInt(servers.size());
		return servers.get(index);
	} 
}
