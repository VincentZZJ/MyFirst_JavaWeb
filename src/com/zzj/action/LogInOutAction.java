package com.zzj.action;

import com.zzj.actionSuper.*;
import com.zzj.model.*;
import com.zzj.dao.*;

public class LogInOutAction extends MySuperAction{
	protected UserSingle user;
	//��װ�û���ȡ�����õķ���
	public UserSingle getUser() {
		return user;
	}

	public void setUser(UserSingle user) {
		this.user = user;
	}
	//��֤��ǰ��¼״̬
	public String isLogin(){
		Object ob = session.get("loginUser");
		if(ob==null || !(ob instanceof UserSingle)){
			return INPUT;
		}else{
			return LOGIN;
		}
	}
	//��֤��������Ϣ
	public void validateLogin(){
		String username = user.getUsername();
		String password = user.getPassword();
		if(username==null || username.equals("")){
			addFieldError("nameError",getText("�������˺�"));
		}
		if(password==null || password.equals("")){
			addFieldError("passwordError",getText("����������"));
		}
	}
	//��¼ҵ����
	public String Login(){
		String sql = "select * from tb_user where user_name = ? and user_password = ?";
		Object[] params = {user.getUsername(),user.getPassword()};
		OpDB myOp = new OpDB();
		if(myOp.LogOn(sql, params)){
			session.put("loginUser", user);
			return LOGIN;
		}else{
			addFieldError("loginE",getText("����������"));
			return INPUT;
		}
	}
	//�˳���¼ҵ����
	public String Logout(){
		session.clear();
		return "logout";
	}
	
	
}
