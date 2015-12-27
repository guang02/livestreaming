package edu.sysu.netlab.livestreaming.controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

import edu.sysu.netlab.livestreaming.model.Notice;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

public class NoticeController extends Controller {
	//test dev
	public void index() {
		String para = getPara(0);

		ResponseJson rj = new ResponseJson();
		if(para==null){

			String sql = "select id,title,posterUrl from Notice";
			List<Notice> lists = Notice.dao.find(sql);
			
			rj.setCode(ResponseCode.Success)
			  .setData(JsonKit.toJson(lists))
			  .setMessage("获取成功!");

		}else{
			Notice notice = Notice.dao.findById(para);
			
			rj.setCode(ResponseCode.Success)
			  .setData(JsonKit.toJson(notice));
		}
		
		renderJson(rj.toString());
	}
	
	
	
	
}
