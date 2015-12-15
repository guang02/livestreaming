package edu.sysu.netlab.livestreaming.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class CrossFleidHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		response.setCharacterEncoding("UTF8");
		response.setContentType("text/html;charset=UTF-8");		
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        nextHandler.handle(target, request, response, isHandled);

	}

}
