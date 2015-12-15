package edu.sysu.netlab.livestreaming.controller;

import java.util.Objects;

import com.jfinal.core.Controller;

import edu.sysu.netlab.livestreaming.model.LiveRoom;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

/**
 * 该模块用于流媒体服务器向应用服务
 * 1. 验证各类信息
 * 2. 统计自身情况
 * 3. 等等
 * @author JoshuaShaw
 *
 */
public class RtmpServerController extends Controller{
	
	/**
	 * 检查合法的发布者
	 * 
	 * @param key 发布者的唯一性key
	 */
	public void publisher() throws Exception {
		ResponseJson rj = new ResponseJson();
		
		String key = getPara("key");
		String sql = "select * from LiveRoom where key=?";
		LiveRoom liveRoom = LiveRoom.dao.findFirst(sql, key);
		
		try {
			Objects.requireNonNull(liveRoom);
			rj.setCode(ResponseCode.Success)
			  .setMessage("该发布者合法！");
			
		} catch (NullPointerException e) {
			rj.setCode(ResponseCode.PublisherError)
			  .setMessage("该发布者非法！");
			
		} finally {
			renderJson(rj.toString());
		}
	}
}
