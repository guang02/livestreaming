package edu.sysu.netlab.livestreaming.controller;

import static edu.sysu.netlab.livestreaming.model.User.dao;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import edu.sysu.netlab.livestreaming.interceptor.LoginInterceptor;
import edu.sysu.netlab.livestreaming.model.User;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;
import edu.sysu.netlab.livestreaming.validator.EmailValidator;

/**
 * 该类包含功能有<br>
 * 1. 注册账户<br>
 * 2. 登录账户<br>
 * @author JoshuaShaw
 * @version 0.1
 */
public class UserController extends Controller {
	
	/**
	 * 由于调试，部署时删除
	 */
	public void index() throws Exception {
		renderHtml("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>注册</h1><br><form name=\"input\" action=\"./user/register\" method=\"post\">email: <input type=\"text\" name=\"email\" />nickName: <input type=\"text\" name=\"nickName\" />password: <input type=\"text\" name=\"password\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>");
	}
	
	/**
	 * 注册账户
	 * @param email 邮箱
	 * @param nickName 用户昵称
	 * @param password 密码
	 */
	@Before(EmailValidator.class)
	public void register() throws Exception {
		
		ResponseJson rj = new ResponseJson();
		
		String email = getPara("email");
		String nickName = getPara("nickName");
		String password = getPara("password");
					
		try {
			String sql = "select * from User where email=?";
			User user = dao.findFirst(sql, email);
			user.get("id");
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("Email已经被注册！");
		
		}catch(Exception e) {
			dao.clear();
			User user = dao.set("email", email)
					       .set("nickName", nickName)
					       .set("password", password);		
			user.save();		
			setSessionAttr("userId", user.get("id"));			
			rj.setCode(ResponseCode.Success)
			  .setMessage("注册成功！");			
		} finally {
			renderJson(rj.toString());		
		}

	}

	/**
	 * 登录账户
	 * @param email 邮箱
	 * @param password 密码
	 */
	public void login() throws Exception {
		
		String email = getPara("email");
		String password = getPara("password");
		
		ResponseJson rj = new ResponseJson();
		
		try {
			String sql = "select * from User where email=? and password=?";
			User user = dao.findFirst(sql, email, password);
			user.get("id");
			setSessionAttr("userId", user.get("id"));
			
			rj.setCode(ResponseCode.Success)
			  .setMessage("登录成功！");
			
		}catch(Exception e) {
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("Email或者密码错误！");
			
		} finally {
			renderJson(rj.toString());
			
		}
	}
	
	/**
	 * 登出账户
	 */
	@Before(LoginInterceptor.class)
	public void logout() throws Exception {		
		ResponseJson rj = new ResponseJson();
		
		try {
			removeSessionAttr("userId");	
			rj.setCode(ResponseCode.Success)
			  .setMessage("登出成功！");
			
		}catch(Exception e) {
			rj.setCode(ResponseCode.GeneralError)
			  .setMessage("错误！");
			
		} finally {
			renderJson(rj.toString());		
		}
	}
	
	//查看自身信息
	public void me() throws Exception {
		ResponseJson rj = new ResponseJson();
		
		try {
			int userId = getSessionAttr("userId");		
			User user = User.dao.findById(userId);
			
			rj.setCode(ResponseCode.Success)
			  .setData(user.toJson())
			  .setMessage("获取个人信息成功！");
			
		} catch (Exception e) {
			rj.setCode(ResponseCode.GeneralError)
			  .setMessage("游客，欢迎！");
			
		} finally {
			renderJson(rj.toString());
			
		}	
	}
	
	/**
	 * @param nickName
	 * @param password
	 */
	@Before(LoginInterceptor.class)
	public void update() throws Exception {
		ResponseJson rj = new ResponseJson();
		try {
			int userId = getSessionAttr("userId");
			User user = User.dao.findById(userId);
			
			if(null!=getPara("nickName"))
				user.set("nickName", getPara("nickName"));
			if(null!=getPara("password"))
				user.set("password", getPara("password"));
			
			user.update();
			
			rj.setCode(ResponseCode.Success)
			  .setMessage("更新数据成功！");

		} catch (Exception e) {
			rj.setCode(ResponseCode.PostDataError)
			  .setMessage("更新数据有误！");
		} finally {
			renderJson(rj.toString());
		}
	}
}
