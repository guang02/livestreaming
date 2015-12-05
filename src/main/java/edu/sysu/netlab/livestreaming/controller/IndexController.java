package edu.sysu.netlab.livestreaming.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	public void index() {
		renderHtml("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>登录</h1><br><form name=\"input\" action=\"./user/login\" method=\"post\">email: <input type=\"text\" name=\"email\" />password: <input type=\"text\" name=\"password\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>");
	}
}

