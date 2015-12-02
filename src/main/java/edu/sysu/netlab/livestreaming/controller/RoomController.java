package edu.sysu.netlab.livestreaming.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;

import edu.sysu.netlab.livestreaming.heartbeat.NotAvailableServerException;
import edu.sysu.netlab.livestreaming.heartbeat.RtmpServer;
import edu.sysu.netlab.livestreaming.heartbeat.ServerDispatcher;
import edu.sysu.netlab.livestreaming.interceptor.LoginInterceptor;
import edu.sysu.netlab.livestreaming.model.GameType;
import edu.sysu.netlab.livestreaming.model.LiveRoom;
import edu.sysu.netlab.livestreaming.model.RecordRoom;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;


/**
 * 该类包含功能有<br>
 * 1. 获取直播房间列表<br>
 * 2. 已登录的用户注册一个直播房间<br>
 * 3. 保存直播录像<br>
 * <br>
 * 注意事项：<br>
 * 1. 除获取直播列表（1）外的其他功能均需要在登录后使用<br>
 * 2. post表示该参数需要post，否则不需要
 * 
 * @author JoshuaShaw
 * @version 0.1
 *
 */
@Before(LoginInterceptor.class)
public class RoomController extends Controller {
	
	private static ServerDispatcher sd = ServerDispatcher.self();
	
	public void index() {
		renderHtml("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><form name=\"input\" action=\"http://localhost:8080/room/registerLiveRoom\" method=\"post\">roomName: <input type=\"text\" name=\"roomName\" />roomDescription: <input type=\"textarea\" name=\"roomDescription\" />gameType: <input type=\"text\" name=\"gameType\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>");
	}
	
	/**
	 * 已登录的用户注册直播房间<br>
	 * <br>
	 * @param userId （用户ID）该参数保存在session中
	 * @param roomName （房间名称）post
	 * @param roomDescription （房间描述）post
	 * @param gameType （游戏类型）post
	 * 
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	public void registerLiveRoom() {
		
		//构造响应json
		ResponseJson rj = new ResponseJson();
		
		//从client端获取post数据
		int userId = getSessionAttr("userId");
		String roomName = getPara("roomName");
		String roomDescription = getPara("roomDescription");
		int gameType = getParaToInt("gameType");
		Date date = new Date();
		Timestamp createTime = new Timestamp(date.getTime());
		
		//构建发布密钥publish key
		StringBuilder preKey = new StringBuilder();
		preKey.append(userId)
		      .append(roomName)
		      .append(gameType)
		      .append(date);
		String key = HashKit.sha256(preKey.toString());
		
		//获取可用的RTMP服务器
		String ip = getRequest().getRemoteAddr();
		InetAddress publisherIp;
		try {
			//一般来说，该方法不会抛出异常
			publisherIp = InetAddress.getByName(ip);	
			RtmpServer<InetAddress, LiveRoom> server = sd.requestForServer(publisherIp);
			String pushUrl = server.getURI().toString();
			String serverIp = server.getServerIp().toString();
			String posterUrl = new StringBuilder().append("http://")
					                              .append(serverIp)
					                              .append("/posters/")
					                              .append(key)
					                              .append(".gif")
					                              .toString();
		
			
			LiveRoom liveRoom = LiveRoom.dao.set("key", key)
		                                .set("roomName", roomName)
							            .set("roomDescription", roomDescription)
							            .set("watchCount", 0)
							            .set("userId", userId)
							            .set("posterUrl", posterUrl)
							            .set("gameType", gameType)
							            .set("pushUrl", pushUrl)
							            .set("serverIp", serverIp)
							            .set("createTime", createTime);
			liveRoom.save();
			Long id = liveRoom.getLong("id");
			key = liveRoom.get("key");		
			server.addClient(publisherIp, liveRoom);
		
			rj.setCode(ResponseCode.Success)
			  .setData(JsonKit.toJson(liveRoom))
			  .setMessage("直播房间创建成功！");
			
		} catch (UnknownHostException e){
			rj.setCode(ResponseCode.GeneralError)
			  .setMessage("您的IP地址异常！");
			e.printStackTrace();
			
		} catch (NotAvailableServerException e) {
			rj.setCode(ResponseCode.RtmpServerError)
			  .setMessage("RTMP服务器内部错误！");
			e.printStackTrace();
			
		} finally {
			renderJson(rj.toString());
		}
	}

	/**
	 * 获取直播房间列表<br>
	 * <br>
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	@Clear(LoginInterceptor.class)
	public void liveRooms() {
		String sql = "select * from LiveRoom";
		List<LiveRoom> liveRooms = LiveRoom.dao.find(sql);
		
		ResponseJson rj = new ResponseJson();
		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson( liveRooms ))
		  .setMessage("获取直播房间成功！");
		
		renderJson(rj.toString());
	}
	
	/**
	 * 保存直播录像<br>
	 * 
	 * @param id （直播房间ID）
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	public void liveRoomToRecordRoom() {
		
		ResponseJson rj = new ResponseJson();
		
		int id = getParaToInt("id");
		LiveRoom liveRoom = LiveRoom.dao.findById(id);
			
		try {
			RecordRoom.dao.fromLiveRoom(liveRoom);
			liveRoom.delete();
			rj.setCode(ResponseCode.Success)
			  .setMessage("录像保存成功！");
			
		} catch (NullPointerException e){
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("不存在该直播房间！");
			
		} finally {
			renderJson(rj.toString());
		}
	}
}