package com.zzj.action;

import java.util.*;

import com.zzj.actionSuper.*;
import com.zzj.model.*;
import com.zzj.dao.*;

public class AdminAction extends AdminSuperAction{
	//��Ϣ�б�չʾ
	public String ListShow(){
		request.setAttribute("mainPage","../info/listshow.jsp");
		session.remove("adminOP");
		
		int infoType=showType.getInfoType();
		String stateType=showType.getStateType();
		String payforType=showType.getPayforType();

		session.put("infoType",Integer.valueOf(infoType));  				//������ѡ��ġ���Ϣ��𡱵�ѡ��
		session.put("payforType",payforType);								//������ѡ��ġ�����״̬����ѡ��
		session.put("stateType",stateType);									//������ѡ��ġ����״̬����ѡ��	
		
		String sqlall = "";
		String sqlsub = "";
		Object[] params = null;
		String mark="";
		int perR = 3;
		
		//����ͨ���жϹ���Ա���ֲ�ͬѡ�������
		if(!stateType.equals("all")&&!payforType.equals("all")){	//��ͬʱѡ�����״̬�͸���״̬
			mark="1";
			sqlall = "select * from tb_info where info_type=? and info_state=? and info_payfor=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_state=? and info_payfor=? order by info_date desc limit " + perR;
			params = new Object[3];
			params[0] = Integer.valueOf(infoType);
			params[1] = stateType;
			params[2] = payforType;
		}else if(stateType.equals("all")&&payforType.equals("all")){	//ͬʱѡ�����״̬�͸���״̬
			mark="2";
			sqlall = "select * from tb_info where info_type=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? order by info_date desc limit " + perR;
			params = new Object[1];
			params[0] = Integer.valueOf(infoType);
		}else if(stateType.equals("all")){			//ѡ�����״̬
			mark="3";
			sqlall = "select * from tb_info where info_type=? and info_payfor=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_payfor=? order by info_date desc limit " + perR;
			params = new Object[2];
			params[0] = Integer.valueOf(infoType);
			params[1] = payforType;
		}else{										//ѡ������״̬
			mark="4";
			sqlall = "select * from tb_info where info_type=? and info_state=? order by info_date desc";
			sqlsub = "select * from tb_info where info_type=? and info_state=? order by info_date desc limit " + perR;
			params = new Object[2];
			params[0] = Integer.valueOf(infoType);
			params[1] = stateType;
		}
		//��ҳ����ҵ��
		String strCurrentP = request.getParameter("showpage");
		String gowhich = "admin_ListShow.action";
		
		OpDB myOp = new OpDB();
		CreatePage createPage = myOp.OpCreatePage(sqlall, params, perR, strCurrentP, gowhich);
		//��ȡ��ǰҳ
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
	//����Ϣ������˲�����ҵ��
		public String Check(){
			System.out.println("����Check()");
			session.put("adminOp", "Check");	//��session�м�¼��ǰҵ�����
			String checkID = request.getParameter("checkID");
			String sql = "update tb_info set info_state=1 where id=?";
			Object[] params = {checkID};
			OpDB myOp = new OpDB();
			int i = myOp.OpUpdate(sql, params);
			System.out.println("  " + i);
			if(i>0){			//��˳ɹ�
				return "checkSuccess";
			}else{
				comebackState();
				addFieldError("AdminCheckUnSuccess",getText("���ʧ��"));
				request.setAttribute("mainPage", "/pages/error.jsp");
				return "UnSuccess";
			}
		}
		
		//����Ϣ���и���ȷ��������ҵ��
		public String SetMoneyShow(){
			request.setAttribute("mainPage","../info/moneyshow.jsp");		
			
			String moneyID=request.getParameter("moneyID");
			if(moneyID==null||moneyID.equals(""))
				moneyID="-1";
			
			String sql="SELECT * FROM tb_info WHERE (id = ?)";
			Object[] params={moneyID};
			
			OpDB myOp=new OpDB();
			infoSingle=myOp.OpSingleShow(sql, params);		
			if(infoSingle==null){			//��Ϣ������
				request.setAttribute("mainPage","/pages/error.jsp");
				addFieldError("AdminShowNoExist",getText("��Ϣ������"));
			}
			return SUCCESS;
		}
		
		//����Ϊ������Ϣ����
		public String SetMoney(){		
			String moneyID=request.getParameter("moneyID");
			if(moneyID==null||moneyID.equals(""))
				moneyID="-1";
			String sql="UPDATE tb_info SET info_payfor=1 WHERE (id = ?)";
			Object[] params={Integer.valueOf(moneyID)};
			
			OpDB myOp=new OpDB();
			int i=myOp.OpUpdate(sql, params);
			if(i>0){								//��Ϣ�������óɹ�			
				addFieldError("AdminSetMoneySuccess",getText("���ѳɹ���"));			
				request.setAttribute("mainPage","/pages/error.jsp");
				return "setMoneySuccess";			
			}
			else{									//��Ϣ��������ʧ��
				addFieldError("AdminSetMoneyUnSuccess",getText("����ʧ��"));			
				request.setAttribute("mainPage","/pages/error.jsp");
				return "UnSuccess";
			}
		}
	//��֤������Ϣ����ʱ����Ϣ
		public void validateSetMoneyShow() {
			request.setAttribute("mainPage","/pages/error.jsp");
			
			String moneyID=request.getParameter("moneyID");		
			if(moneyID==null||moneyID.equals("")){				//û��������ϢIDֵ
				addFieldError("moneyIDError",getText("û��������ϢID"));
			}
			else{												//��֤�������ϢIDֵ�Ƿ�Ϊ���ָ�ʽ			
				try{
					int id=Integer.parseInt(moneyID);
					if(id<0)									//��IDΪ����
						addFieldError("moneyIDError",getText("ID����ȷ"));
				}catch(NumberFormatException e){
					addFieldError("moneyIDError",getText("ID����ȷ"));
					e.printStackTrace();
				}
			}
		}
	
	
	
	//������Ϣ����ϸ������ʾ
	public String CheckShow(){
		System.out.println("����CheckShow()");
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
			addFieldError("AdminShowNoExist",getText("��Ϣ������"));
		}
		return SUCCESS;
	}
	
	//����Ϣ����ɾ��������ҵ��
	public String Delete(){
		session.put("adminOp", "Delete");
		String deleteID = request.getParameter("deleteID");
		String sql = "delete from tb_info where id=?";
		Object[] params = {deleteID};
		OpDB myOp = new OpDB();
		int i = myOp.OpUpdate(sql, params);
		if(i>0){			//ɾ���ɹ�
			//ɾ������һ�����ݺ��ú����id�Լ�1
			myOp.OpUpdateId(deleteID);
			return "deleteSuccess";
		}else{
			comebackState();
			addFieldError("AdminDeleteUnSuccess",getText("ɾ��ʧ��"));
			request.setAttribute("mainPage", "/pages/error.jsp");
			return "UnSuccess";
		}
	}
	
	//��Ϣ�б���ʾǰ�ı���֤
	public void validateListShow(){
		request.setAttribute("mainPage","/pages/error.jsp");
		
		String adminOP=(String)session.get("adminOP");
		if(adminOP==null)
			adminOP="";
		//����ǽ����ˡ�ͨ����ˡ���ɾ����Ϣ����������������ListShow()������ʾ��Ϣ�б�����Ҫ�ָ�֮ǰѡ��ġ���ʾ��ʽ���͡���Ϣ���״̬���Ӷ�������ListShow()�����в�ѯ�����������ļ�¼
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
			
			if(getInfoType<=0){						//û��ѡ����Ϣ���
				addFieldError("AdminListNoType",getText("û��ѡ����Ϣ���"));
			}
			else{
				if(getPayforType==null||getPayforType.equals("")){		//û��ѡ�񡰸���״̬��ѡ��
					addFieldError("AdminListNoPayForType",getText("û��ѡ�񡰸���״̬��ѡ��"));
				}
				if(getStateType==null||getStateType.equals("")){			//û��ѡ�����״̬��ѡ��
					addFieldError("AdminListNoStateType",getText("û��ѡ�����״̬��ѡ��"));
				}
			}			
		}
	}
	//���ڻָ�����Ա��������Ϣ����ʾ״̬
	private void comebackState(){
		/* ��ȡsession�б����ѡ��״̬��
		 * ��ѡ��״̬������session�У�
		 * ���ڹ���Ա��������ʾ����ť�����б���ʾʱ��
		 * ��ListShow()������ʵ�ֵ�
		 */
		Integer getInfoType=(Integer)session.get("infoType");
		String getPayForType=(String)session.get("payforType");
		String getStateType=(String)session.get("stateType");
		
		/* �ָ�ѡ���״̬ */
		if(getPayForType!=null&&getStateType!=null&&getInfoType!=null){			
			showType.setInfoType(getInfoType.intValue());	
			showType.setPayforType(getPayForType);
			showType.setStateType(getStateType);			
		}
	}
}
