package edu.sysu.netlab.livestreaming.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

import edu.sysu.netlab.livestreaming.controller.GameTypeController;
import edu.sysu.netlab.livestreaming.controller.LiveRoomController;
import edu.sysu.netlab.livestreaming.controller.NoticeController;
import edu.sysu.netlab.livestreaming.controller.RecordRoomController;
import edu.sysu.netlab.livestreaming.controller.RtmpServerController;
import edu.sysu.netlab.livestreaming.controller.UserController;
import edu.sysu.netlab.livestreaming.handler.CrossFleidHandler;
import edu.sysu.netlab.livestreaming.handler.XssHandler;
import edu.sysu.netlab.livestreaming.interceptor.ExceptionInterceptor;
import edu.sysu.netlab.livestreaming.model.GameType;
import edu.sysu.netlab.livestreaming.model.LiveRoom;
import edu.sysu.netlab.livestreaming.model.Notice;
import edu.sysu.netlab.livestreaming.model.RecordRoom;
import edu.sysu.netlab.livestreaming.model.RtmpServer;
import edu.sysu.netlab.livestreaming.model.User;

public class JFinalProjectConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
	}

	@Override
	public void configRoute(Routes me) {

		me.add("/user", UserController.class);
		me.add("/live", LiveRoomController.class);
		me.add("/record", RecordRoomController.class);
		me.add("/gameType", GameTypeController.class);
		me.add("/check", RtmpServerController.class);
		me.add("/notice", NoticeController.class);
		
	}

	@Override
	public void configPlugin(Plugins me) {
		this.loadPropertyFile("config.properties");
		
		//---druid
		DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		me.add(dp);
		//---End of druid
		
		//---AR
		ActiveRecordPlugin activePlugin = new ActiveRecordPlugin(dp);
		me.add(activePlugin);
		activePlugin.addMapping("User", User.class);
		activePlugin.addMapping("LiveRoom", LiveRoom.class);
		activePlugin.addMapping("RecordRoom", RecordRoom.class);
		activePlugin.addMapping("GameType", GameType.class);
		activePlugin.addMapping("RtmpServer", RtmpServer.class);
		activePlugin.addMapping("Notice", Notice.class);
		//---End of AR
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new ExceptionInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new CrossFleidHandler());
		me.add(new XssHandler(""));
	}
	
	@Override
	public void afterJFinalStart() {
	}
	
	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8081, "/", 5);
	}
		
}
