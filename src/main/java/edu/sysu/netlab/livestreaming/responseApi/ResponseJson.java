package edu.sysu.netlab.livestreaming.responseApi;

import com.alibaba.fastjson.JSONObject;

public class ResponseJson {
	
	private JSONObject result = null;
	
	public ResponseJson(){
		result = new JSONObject();
		
		setCode(ResponseCode.Success);
		setData(new JSONObject());
		setMessage("");
	}
	
	public ResponseJson setCode(ResponseCode code) {
		this.result.put("code", code.value());
		return this;
	}
	
	public ResponseJson setData(Object object) {
		this.result.put("data", object);
		return this;
	}
	
	public ResponseJson setMessage(String message) {
		this.result.put("message", message);
		return this;
	}
	
	public String toString(){
		return result.toString();
	}
}
