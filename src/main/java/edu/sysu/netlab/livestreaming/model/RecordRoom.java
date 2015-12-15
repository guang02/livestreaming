package edu.sysu.netlab.livestreaming.model;

import com.jfinal.plugin.activerecord.Model;


public class RecordRoom extends Model<RecordRoom> {
	public static final RecordRoom dao = new RecordRoom();
	
	@SuppressWarnings("finally")
	public boolean fromLiveRoom(LiveRoom liveRoom) throws NullPointerException{
		boolean success = false;
		try{
			new RecordRoom()
			   .set("key", liveRoom.get("key"))
               .set("roomName", liveRoom.get("roomName"))
		       .set("roomDescription", liveRoom.get("roomDescription"))
		       .set("watchCount", liveRoom.get("watchCount"))
		       .set("userId", liveRoom.get("userId"))
		       .set("posterUrl", liveRoom.get("posterUrl"))
		       .set("gameType", liveRoom.get("gameType"))
		       .set("createTime", liveRoom.get("createTime"))
		       .set("playUrl", new StringBuilder().append("rtmp://")
		                		                  .append((String)liveRoom.get("serverIp"))
		                		                  .append("/videos/")
		                		                  .toString()
		                                          )
		       .save();
			
			success = true;
		}
		catch(Exception e){
			//e.printStackTrace();
			throw e;
			
		}finally {
			return success;
		}
	}
}
