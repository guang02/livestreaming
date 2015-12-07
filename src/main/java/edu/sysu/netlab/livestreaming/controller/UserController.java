package edu.sysu.netlab.livestreaming.controller;

import static edu.sysu.netlab.livestreaming.model.User.dao;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import edu.sysu.netlab.livestreaming.handler.XssHandler;
import edu.sysu.netlab.livestreaming.model.User;
import edu.sysu.netlab.livestreaming.responseApi.ResponseCode;
import edu.sysu.netlab.livestreaming.responseApi.ResponseJson;
import edu.sysu.netlab.livestreaming.validator.EmailValidator;

/**
 * 该类包含功能有<br>
 * 1. 注册账户<br>
 * 2. 登录账户<br>
 * <br>
 * 注意事项：<br>
 * 1. post表示该参数需要post，否则不需要
 * 
 * @author JoshuaShaw
 * @version 0.1
 *
 */
public class UserController extends Controller {
	
	public void index() {
		renderHtml("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head><body><h1>注册</h1><br><form name=\"input\" action=\"/user/register\" method=\"post\">email: <input type=\"text\" name=\"email\" />nickName: <input type=\"text\" name=\"nickName\" />password: <input type=\"text\" name=\"password\" /><input type=\"submit\" value=\"Submit\" /></form></body></html>");
	}
	
	/**
	 * 注册账户<br>
	 * <br>
	 * 
	 * @param email （邮箱）该格式需要被验证 post
	 * @param nickName （用户昵称）post
	 * @param password （密码）post
	 * @author JoshuaShaw
	 * @see UserController
	 * @see XssHandler
	 * 
	 */
	@Before(EmailValidator.class)
	public void register() {
		
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
			//e.printStackTrace();
			
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
	 * 登录账户<br>
	 * <br>
	 * 
	 * @param email （邮箱）post
	 * @param password （密码）post
	 * @author JoshuaShaw
	 * @see UserController
	 */
	public void login(){
		
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
	
	public void me() {
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
			e.printStackTrace();
			
		} finally {
			renderJson(rj.toString());
			
		}
		
	}
}
