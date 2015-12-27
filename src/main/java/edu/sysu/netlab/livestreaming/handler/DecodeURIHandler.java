package edu.sysu.netlab.livestreaming.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class DecodeURIHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		System.out.println("Global");
		/**
		 * convert resquest's parameters to UTF-8
		 */
		Map<String, String[]> maps = request.getParameterMap();
		for(Map.Entry<String, String[]> entry : maps.entrySet()) {
			List<String> list = new ArrayList<String>();
			for(String para : entry.getValue()) {
				try {
					String result = URLDecoder.decode(para, "UTF-8");
					list.add(result);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} finally{
					String [] paras = list.toArray(new String[list.size()]);
					entry.setValue(paras);
				}
			}
		}
	}

}
