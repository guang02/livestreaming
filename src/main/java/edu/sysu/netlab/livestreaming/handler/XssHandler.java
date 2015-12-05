package edu.sysu.netlab.livestreaming.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.jfinal.handler.Handler;

/**
 * 使用jsoup过滤敏感的html代码<br>
 * <br>
 * 
 * @author JoshuaShaw
 * @version 0.1
 * @see Jsoup
 */
public class XssHandler extends Handler {
    
    // 排除的url，使用的target.startsWith匹配的
    private String exclude;
      
    public XssHandler(String exclude) {
        this.exclude = exclude;
    }
  
    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        
    	// 对于非静态文件，和非指定排除的url实现过滤
        if (target.indexOf(".") == -1 && !target.startsWith(exclude)){
            request = new HttpServletRequestWrapper(request);
        }
        nextHandler.handle(target, request, response, isHandled);
    }
    
    static public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper{
  	  
        public HttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }
      
        @Override
        public String getParameter(String name) {
            return getBasicHtmlandimage(super.getParameter(name));
        }
          
        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (null == values){
                return null;
            }
            for (int i = 0; i < values.length; i++) {
                values[i] = getBasicHtmlandimage(values[i]);
            }
            return values;
        }
          
        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> paraMap = super.getParameterMap();
            // 对于paraMap为空的直接return
            if (null == paraMap || paraMap.isEmpty()) {
                return paraMap;
            }
            for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (null == values) {
                    continue;
                }
                String[] newValues  = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    newValues[i] = getBasicHtmlandimage(values[i]);
                }
                paraMap.put(key, newValues);
            }
            return paraMap;
        }
        
        public static String getBasicHtmlandimage(String html) {
            if (html == null)
                return null;
            return Jsoup.clean(html, Whitelist.basicWithImages());
        }
    }

}
