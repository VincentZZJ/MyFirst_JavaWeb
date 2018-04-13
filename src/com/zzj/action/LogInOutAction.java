package com.zzj.action;

import com.zzj.actionSuper.*;
import com.zzj.model.*;
import com.zzj.dao.*;

public class LogInOutAction extends MySuperAction{
	protected UserSingle user;
	//封装用户获取和设置的方法
	public UserSingle getUser() {
		return user;
	}

	public void setUser(UserSingle user) {
		this.user = user;
	}
	//验证当前登录状态
	public String isLogin(){
		Object ob = session.get("loginUser");
		if(ob==null || !(ob instanceof UserSingle)){
			return INPUT;
		}else{
			return LOGIN;
		}
	}
	//验证表单输入信息
	public void validateLogin(){
		String username = user.getUsername();
		String password = user.getPassword();
		if(username==null || username.equals("")){
			addFieldError("nameError",getText("请输入账号"));
		}
		if(password==null || password.equals("")){
			addFieldError("passwordError",getText("请输入密码"));
		}
	}
	//登录业务处理
	public String Login(){
		String sql = "select * from tb_user where user_name = ? and user_password = ?";
		Object[] params = {user.getUsername(),user.getPassword()};
		OpDB myOp = new OpDB();
		if(myOp.LogOn(sql, params)){
			session.put("loginUser", user);
			return LOGIN;
		}else{
			addFieldError("loginE",getText("请重新输入"));
			return INPUT;
		}
	}
	//退出登录业务处理
	public String Logout(){
		session.clear();
		return "logout";
	}
	
	
}
