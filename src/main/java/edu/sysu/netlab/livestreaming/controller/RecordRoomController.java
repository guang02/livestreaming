package edu.sysu.netlab.livestreaming.controller;

import java.util.List;
import java.util.Objects;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import edu.sysu.netlab.livestreaming.interceptor.LoginInterceptor;
import edu.sysu.netlab.livestreaming.model.RecordRoom;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

@Before(LoginInterceptor.class)
public class RecordRoomController extends Controller{
	
	private final static int PAGE_SIZE = 12;
		
	/**
	 * 查看全部录像
	 * @param page 页码。可选的，不传递该参数时默认为1
	 */
	@Clear(LoginInterceptor.class)
	public void rooms() throws Exception {

		ResponseJson rj = new ResponseJson();
		
		String select = "SELECT RR.key, RR.roomName, RR.roomDescription, RR.watchCount, RR.posterUrl, RR.playUrl, RR.createTime, GT.`name` as gameTypeName, U.nickName";
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
	 * 查看特定录像
	 * @param key 
	 */
	@Clear(LoginInterceptor.class)
	public void room() throws Exception {

		ResponseJson rj = new ResponseJson();
		
		String key = getPara("key");
		String sql = "SELECT RR.key, RR.roomName, RR.roomDescription, RR.watchCount, RR.posterUrl, RR.playUrl, RR.createTime, GT.`name` as gameTypeName, U.nickName from RecordRoom as RR, GameType as GT, `User` as U WHERE RR.gameType=GT.id and RR.userId=U.id and RR.key=?";
		
		Record recordRoom = Db.findFirst(sql, key);
		
		try {
			Objects.requireNonNull(recordRoom);
			
			//非一致性的计数
			RecordRoom counter = RecordRoom.dao.findFirst("SELECT * FROM RecordRoom WHERE RecordRoom.`key`=?", key);
			counter.set("watchCount", counter.getLong("watchCount") + 1).update();
			
			rj.setCode(ResponseCode.Success)
			  .setData(JsonKit.toJson( recordRoom ))
			  .setMessage("获取录像房间成功！");
			
		} catch (NullPointerException e) {
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("不存在该录像房间！");
			
		} finally {
			renderJson(rj.toString());
		}
	}
	
	/**
	 * 查看自己的录像
	 * @param page 页码。可选的，不传递该参数时默认为1
	 */
	public void me() throws Exception {
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
