package edu.sysu.netlab.livestreaming.controller;

import com.jfinal.core.Controller;

import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;

public class NoticeController extends Controller {
	//test dev
	public void index() {
		ResponseJson rj = new ResponseJson();
		rj.setCode(ResponseCode.Success);
		renderJson(rj);
	}
}
