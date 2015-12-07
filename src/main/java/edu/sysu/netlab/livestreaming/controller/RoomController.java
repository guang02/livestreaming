package edu.sysu.netlab.livestreaming.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.common.base.Objects;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import edu.sysu.netlab.livestreaming.interceptor.LoginInterceptor;
import edu.sysu.netlab.livestreaming.model.LiveRoom;
import edu.sysu.netlab.livestreaming.model.RecordRoom;
import edu.sysu.netlab.livestreaming.model.RtmpServer;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;


/**
 * 该类包含功能有<br>
 * 1. 获取直播房间列表<br>
 * 2. 已登录的用户注册一个直播房间<br>
 * 3. 保存直播录像<br>
 * 4. 查看所有录像<br>
 * 5. 查看自己的录像<br>
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
	final static int PAGE_SIZE = 12;
	
	
	
	public void index() {
		renderHtml("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>创建房间</h1><br><form name=\"input\" action=\"/room/register\" method=\"post\">roomName: <input type=\"text\" name=\"roomName\" />roomDescription: <input type=\"textarea\" name=\"roomDescription\" />gameType: <input type=\"text\" name=\"gameType\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>");
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
	public void register() {
		
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
		preKey.append(userId).append(roomName).append(gameType).append(date);
		String key = HashKit.sha256(preKey.toString());
		
		//获取可用的RTMP服务器
		String ip = getRequest().getRemoteAddr();

		try {
			//一般来说，该方法不会抛出异常
			@SuppressWarnings("unused")
			InetAddress publisherIp = InetAddress.getByName(ip);	
			RtmpServer server = RtmpServer.getRtmpServer();
			String serverIp = server.getStr("ip");
			String pushUrl = server.getStr("pushUrl");
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
			key = liveRoom.get("key");		
		
			rj.setCode(ResponseCode.Success)
			  .setData(JsonKit.toJson(liveRoom))
			  .setMessage("直播房间创建成功！");
			
		} catch (UnknownHostException e){
			rj.setCode(ResponseCode.GeneralError)
			  .setMessage("您的IP地址异常！");
			e.printStackTrace();
			
		} catch (ActiveRecordException e) {
			rj.setCode(ResponseCode.GeneralError)
			  .setMessage("已经有属于你的直播房间！");
			e.printStackTrace();
			
		} catch (NullPointerException e) {
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("post数据有误！");
			e.printStackTrace();
			
		} catch (Exception e) {
			rj.setCode(ResponseCode.RtmpServerError)
			  .setMessage("RTMP服务器有误！");
			e.printStackTrace();
			
		} finally {
			renderJson(rj.toString());
		}
	}

	/**
	 * 获取直播房间列表<br>
	 * <br>
	 * 
	 * @param page 页码。可选的，不传递该参数时默认为1
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	@Clear(LoginInterceptor.class)
	public void liveRooms() {
		ResponseJson rj = new ResponseJson();
		
		String select = "SELECT LR.`key`, LR.roomName, LR.watchCount, LR.posterUrl, LR.pushUrl, LR.createTime, GT.`name` as gameTypeName, U.nickName";
		String sqlExceptSelect = "from LiveRoom as LR, GameType as GT, `User` as U WHERE LR.gameType=GT.id and LR.userId=U.id";
		
		//没有page的话，默认page=1
		Integer page = (page=getParaToInt("page")) != null ? page:1;
		
		List<Record> liveRooms = Db.paginate(page, PAGE_SIZE, select, sqlExceptSelect)
				                 .getList();

		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson( liveRooms ))
		  .setMessage("获取录像房间成功！");
		
		renderJson(rj.toString());
	}
	
	/**
	 * 保存直播录像<br>
	 * <br>
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	public void save() {
		
		ResponseJson rj = new ResponseJson();
		
		String sql = "select * from LiveRoom where userId=?";
		int userId = getSessionAttr("userId");
		LiveRoom liveRoom = LiveRoom.dao.findFirst(sql, userId);
			
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

	/**
	 *  查找自己创建的直播房间详细信息<br>
	 *  <br> 
	 *  @author JoshuaShaw
	 *  @see RoomController
	 */
	public void liveRoom() {
		ResponseJson rj = new ResponseJson();
		
		int userId = getSessionAttr("userId");
		String sql = "select * from LiveRoom where userId=?";
		LiveRoom liveRoom = LiveRoom.dao.findFirst(sql, userId);
		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson(liveRoom))
		  .setMessage("获取成功！");
		
		renderJson(rj.toString());
		
	}
	
	/**
	 * 查看全部录像<br>
	 * <br>
	 * @param page 页码。可选的，不传递该参数时默认为1
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	@Clear(LoginInterceptor.class)
	public void recordRooms() throws Exception{

		ResponseJson rj = new ResponseJson();
		
		String select = "SELECT RR.key, RR.roomName, RR.watchCount, RR.posterUrl, RR.playUrl, RR.createTime, GT.`name` as gameTypeName, U.nickName";
		String sqlExceptSelect = "from RecordRoom as RR, GameType as GT, `User` as U WHERE RR.gameType=GT.id and RR.userId=U.id";
		
		//没有page的话，默认page=1
		Integer page = (page=getParaToInt("page")) != null ? page:1;
		
		List<Record> recordRooms = Db.paginate(page, PAGE_SIZE, select, sqlExceptSelect)
				                 .getList();

		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson( recordRooms ))
		  .setMessage("获取录像房间成功！");
		
		renderJson(rj.toString());
	}
	
	/**
	 * 查看自己的录像<br>
	 * <br>
	 * @param page 页码。可选的，不传递该参数时默认为1
	 * @author JoshuaShaw
	 * @see RoomController
	 */
	public void recordRoom() {
		ResponseJson rj = new ResponseJson();
		
		int userId = getSessionAttr("userId");
		String select = "SELECT RR.key, RR.roomName, RR.watchCount, RR.posterUrl, RR.playUrl, RR.createTime, GT.`name` as gameTypeName";
		String sqlExceptSelect = "from RecordRoom as RR, GameType as GT WHERE RR.gameType=GT.id and userId = ?";
	
		//没有page的话，默认page=1
		Integer page = (page=getParaToInt("page")) != null ?page:1;
		
		List<Record> records = Db.paginate(page, PAGE_SIZE, select, sqlExceptSelect, userId)
				                 .getList();
		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson(records))
		  .setMessage("获取自己的录像房间成功！");
		
		renderJson(rj.toString());
	}
	
}