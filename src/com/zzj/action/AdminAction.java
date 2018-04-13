package com.zzj.action;

import java.util.*;

import com.zzj.actionSuper.*;
import com.zzj.model.*;
import com.zzj.dao.*;

public class AdminAction extends AdminSuperAction{
	//信息列表展示
	public String ListShow(){
		request.setAttribute("mainPage","../info/listshow.jsp");
		session.remove("adminOP");
		
		int infoType=showType.getInfoType();
		String stateType=showType.getStateType();
		String payforType=showType.getPayforType();

		session.put("infoType",Integer.valueOf(infoType));  				//保存已选择的“信息类别”的选项
		session.put("payforType",payforType);								//保存已选择的“付费状态”的选项
		session.put("stateType",stateType);									//保存已选择的“审核状态”的选项	
		
		String sqlall = "";
		String sqlsub = "";
		Object[] params = null;
		String mark="";
		int perR = 3;
		
		//下面通过判断管理员四种不同选择的搜索
		if(!stateType.equals("all")&&!payforType.equals("all")){	//无同时选择审核状态和付费状态
			mark="1";
			sqlall = "select * from tb_info where info_type=? and info_state=? and info_payfor=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_state=? and info_payfor=? order by info_date desc limit " + perR;
			params = new Object[3];
			params[0] = Integer.valueOf(infoType);
			params[1] = stateType;
			params[2] = payforType;
		}else if(stateType.equals("all")&&payforType.equals("all")){	//同时选择审核状态和付费状态
			mark="2";
			sqlall = "select * from tb_info where info_type=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? order by info_date desc limit " + perR;
			params = new Object[1];
			params[0] = Integer.valueOf(infoType);
		}else if(stateType.equals("all")){			//选定审核状态
			mark="3";
			sqlall = "select * from tb_info where info_type=? and info_payfor=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_payfor=? order by info_date desc limit " + perR;
			params = new Object[2];
			params[0] = Integer.valueOf(infoType);
			params[1] = payforType;
		}else{										//选定付费状态
			mark="4";
			sqlall = "select * from tb_info where info_type=? and info_state=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_state=? order by info_date desc limit " + perR;
			params = new Object[2];
			params[0] = Integer.valueOf(infoType);
			params[1] = stateType;
		}
		//分页管理业务
		String strCurrentP = request.getParameter("showpage");
		String gowhich = "admin_ListShow.action";
		
		OpDB myOp = new OpDB();
		CreatePage createPage = myOp.OpCreatePage(sqlall, params, perR, strCurrentP, gowhich);
		//获取当前页
		int currentP = createPage.getCurrentP();
		if(currentP>1){
			int top = (currentP-1)*perR;
			if(mark.equals("1")){
				sqlsub = "select * from tb_info i where info_type=? and info_type=? and info_state=? and info_payfor=? and info_date< "+
						"(select MIN(info_date) from (select * from tb_info where info_type=i.info_type and info_state=i.info_state "
						+ "and info_payfor=i.info_payfor order by info_date desc limit " + top + ")as mindate) order by info_date desc limit " + perR;
			}else if(mark.equals("2")){
				sqlsub = "select * from tb_info i where info_type=? and info_date <" + "(select MIN(info_date) from (select * from tb_info " +
						"where info_type=i.info_type order by info_date desc limit " + top + ")as mindate) order by info_date desc limit " + perR;
			}else if(mark.equals("3")){
				sqlsub = "select * from tb_info i where info_type=? and info_payfor=? and info_date <" + "(select MIN(info_date) from " + 
						"(select * from tb_info where info_type=i.info_type and info_payfor=i.info_payfor order by info_date desc limit " + top +
						")as mindate) order by info_date desc limit " + perR;
			}else{
				sqlsub = "select * from tb_info i where info_type=? and info_state=? and info_date <" + "(select MIN(info_date) from " + 
						"(select * from tb_info where info_type=i.info_type and info_state=i.info_state order by info_date desc limit " + top +
						")as mindate) order by info_date desc limit " + perR;
			}
		}
		List adminlistshow = myOp.OpListShow(sqlsub, params);
		request.setAttribute("adminlistshow",adminlistshow);
		request.setAttribute("createPage", createPage);
		return SUCCESS;
	}
	
	
	//springmvc,springboot,mybatis
	//对信息进行审核操作的业务
		public String Check(){
			System.out.println("进入Check()");
			session.put("adminOp", "Check");	//往session中记录当前业务操作
			String checkID = request.getParameter("checkID");
			String sql = "update tb_info set info_state=1 where id=?";
			Object[] params = {checkID};
			OpDB myOp = new OpDB();
			int i = myOp.OpUpdate(sql, params);
			System.out.println("  " + i);
			if(i>0){			//审核成功
				return "checkSuccess";
			}else{
				comebackState();
				addFieldError("AdminCheckUnSuccess",getText("审核失败"));
				request.setAttribute("mainPage", "/pages/error.jsp");
				return "UnSuccess";
			}
		}
		
		//对信息进行付费确定操作的业务
		public String SetMoneyShow(){
			request.setAttribute("mainPage","../info/moneyshow.jsp");		
			
			String moneyID=request.getParameter("moneyID");
			if(moneyID==null||moneyID.equals(""))
				moneyID="-1";
			
			String sql="SELECT * FROM tb_info WHERE (id = ?)";
			Object[] params={moneyID};
			
			OpDB myOp=new OpDB();
			infoSingle=myOp.OpSingleShow(sql, params);		
			if(infoSingle==null){			//信息不存在
				request.setAttribute("mainPage","/pages/error.jsp");
				addFieldError("AdminShowNoExist",getText("信息不存在"));
			}
			return SUCCESS;
		}
		
		//设置为付费信息操作
		public String SetMoney(){		
			String moneyID=request.getParameter("moneyID");
			if(moneyID==null||moneyID.equals(""))
				moneyID="-1";
			String sql="UPDATE tb_info SET info_payfor=1 WHERE (id = ?)";
			Object[] params={Integer.valueOf(moneyID)};
			
			OpDB myOp=new OpDB();
			int i=myOp.OpUpdate(sql, params);
			if(i>0){								//信息付费设置成功			
				addFieldError("AdminSetMoneySuccess",getText("付费成功！"));			
				request.setAttribute("mainPage","/pages/error.jsp");
				return "setMoneySuccess";			
			}
			else{									//信息付费设置失败
				addFieldError("AdminSetMoneyUnSuccess",getText("付费失败"));			
				request.setAttribute("mainPage","/pages/error.jsp");
				return "UnSuccess";
			}
		}
	//验证付费信息操作时表单信息
		public void validateSetMoneyShow() {
			request.setAttribute("mainPage","/pages/error.jsp");
			
			String moneyID=request.getParameter("moneyID");		
			if(moneyID==null||moneyID.equals("")){				//没有输入信息ID值
				addFieldError("moneyIDError",getText("没有输入信息ID"));
			}
			else{												//验证输入的信息ID值是否为数字格式			
				try{
					int id=Integer.parseInt(moneyID);
					if(id<0)									//若ID为负数
						addFieldError("moneyIDError",getText("ID不正确"));
				}catch(NumberFormatException e){
					addFieldError("moneyIDError",getText("ID不正确"));
					e.printStackTrace();
				}
			}
		}
	
	
	
	//进行信息的详细内容显示
	public String CheckShow(){
		System.out.println("进入CheckShow()");
		request.setAttribute("mainPage","../info/checkshow.jsp");
		comebackState();
		String sql = "select * from tb_info where id=? ";
		String checkID = request.getParameter("checkID");
		if(checkID==null || checkID.equals("")){
			checkID="-1";
		}
		Object[] params = {checkID};
		OpDB myOp = new OpDB();
		infoSingle = myOp.OpSingleShow(sql, params);
		if(infoSingle==null){
			request.setAttribute("mainPage", "/pages/error.jsp");
			addFieldError("AdminShowNoExist",getText("信息不存在"));
		}
		return SUCCESS;
	}
	
	//对信息进行删除操作的业务
	public String Delete(){
		session.put("adminOp", "Delete");
		String deleteID = request.getParameter("deleteID");
		String sql = "delete from tb_info where id=?";
		Object[] params = {deleteID};
		OpDB myOp = new OpDB();
		int i = myOp.OpUpdate(sql, params);
		if(i>0){			//删除成功
			//删除当中一条数据后，让后面的id自减1
			myOp.OpUpdateId(deleteID);
			return "deleteSuccess";
		}else{
			comebackState();
			addFieldError("AdminDeleteUnSuccess",getText("删除失败"));
			request.setAttribute("mainPage", "/pages/error.jsp");
			return "UnSuccess";
		}
	}
	
	//信息列表显示前的表单验证
	public void validateListShow(){
		request.setAttribute("mainPage","/pages/error.jsp");
		
		String adminOP=(String)session.get("adminOP");
		if(adminOP==null)
			adminOP="";
		//如果是进行了“通过审核”或“删除信息”操作后，再来调用ListShow()方法显示信息列表，则需要恢复之前选择的“显示方式”和“信息类别”状态，从而重新在ListShow()方法中查询出符合条件的记录
		if(adminOP.equals("Check")||adminOP.equals("Delete"))    
			comebackState();			
		else{
			int getInfoType=showType.getInfoType();			
			String getPayforType=showType.getPayforType();
			String getStateType=showType.getStateType();
			
			if(getInfoType<=0){
				if(session.get("infoType")!=null){
					getInfoType=(Integer)session.get("infoType");
					showType.setInfoType(getInfoType);
				}
			}			
			if(getPayforType==null||getPayforType.equals("")){
				getPayforType=(String)session.get("payforType");
				showType.setPayforType(getPayforType);
			}
            if(getStateType==null||getStateType.equals("")){
            	getStateType=(String)session.get("stateType");
            	showType.setStateType(getStateType);
			}			
			
			if(getInfoType<=0){						//没有选择“信息类别”
				addFieldError("AdminListNoType",getText("没有选择“信息类别”"));
			}
			else{
				if(getPayforType==null||getPayforType.equals("")){		//没有选择“付费状态”选项
					addFieldError("AdminListNoPayForType",getText("没有选择“付费状态”选项"));
				}
				if(getStateType==null||getStateType.equals("")){			//没有选择“审核状态”选项
					addFieldError("AdminListNoStateType",getText("没有选择“审核状态”选项"));
				}
			}			
		}
	}
	//用于恢复管理员操作后信息的显示状态
	private void comebackState(){
		/* 获取session中保存的选择状态。
		 * 将选择状态保存在session中，
		 * 是在管理员单击“显示”按钮请求列表显示时，
		 * 在ListShow()方法中实现的
		 */
		Integer getInfoType=(Integer)session.get("infoType");
		String getPayForType=(String)session.get("payforType");
		String getStateType=(String)session.get("stateType");
		
		/* 恢复选择的状态 */
		if(getPayForType!=null&&getStateType!=null&&getInfoType!=null){			
			showType.setInfoType(getInfoType.intValue());	
			showType.setPayforType(getPayForType);
			showType.setStateType(getStateType);			
		}
	}
}
