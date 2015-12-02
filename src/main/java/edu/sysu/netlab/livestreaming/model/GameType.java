package edu.sysu.netlab.livestreaming.model;

import com.jfinal.plugin.activerecord.Model;

public class GameType extends Model<GameType> {
	public static final GameType dao = new GameType(); 
	
	/*
	public static String toJsonArray(List<GameType> gameTypes) throws Exception{
		JSONArray ja = new JSONArray();
		gameTypes.forEach(e->ja.add(e.toJson()));
		return ja.toString();
	}
	
	public String toJson() {
		int id = get("id");
		String name = getStr("name");
		String posterUrl = getStr("posterUrl");
		JSONObject jo = new JSONObject();
		jo.put("id", id);
        jo.put("name", name);
        jo.put("posterUrl", posterUrl);
        
		return jo.toString();
	}
	*/
	
}
