package edu.sysu.netlab.livestreaming.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {
	public void index(){
		renderText("Hello world!");
	}
}

