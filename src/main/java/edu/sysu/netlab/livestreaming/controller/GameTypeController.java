package edu.sysu.netlab.livestreaming.controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

import edu.sysu.netlab.livestreaming.model.GameType;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

public class GameTypeController extends Controller {

	public void index() throws Exception {
		String sql = "select * from GameType";
		List<GameType> gameTypes = GameType.dao.find(sql);
		
		ResponseJson rj = new ResponseJson();
		
		rj.setCode(ResponseCode.Success)
		  .setData(JsonKit.toJson( gameTypes ));
		
		renderJson(rj.toString());
	}
}
