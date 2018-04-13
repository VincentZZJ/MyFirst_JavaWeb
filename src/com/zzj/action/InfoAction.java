package com.zzj.action;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.zzj.actionSuper.*;
import com.zzj.dao.*;
import com.zzj.model.*;
import com.zzj.tools.*;

public class InfoAction extends InfoSuperAction{
	public String ListShow(){
		//�˴�������������Ҫ��ת��ҳ��
		request.setAttribute("mainPage", "/pages/show/listshow.jsp");
		//��ȡ��Ϣ�����
		String infoType = request.getParameter("infoType");
		System.out.println(infoType);
		Object[] params = {infoType};
		String sql_payfor = "select * from tb_info where (info_type = ? ) and (info_state = '1' ) and (info_payfor = '1' ) order by info_date desc";
		//�������ݿ�
		OpDB myOp = new OpDB();
		//��ȡ���и�����Ϣ
		List onepayforlist = myOp.OpListShow(sql_payfor, params);
		request.setAttribute("onepayforlist", onepayforlist);
		//��ȡ���������Ϣ
		String sql_free = "select * from tb_info where (info_type = ? ) and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc";
		String sqlFreeSub="";
		int perR=3;			//ÿҳ����ʾ����Ϣ����
		String strCurrentP=request.getParameter("showpage");		//��ǰҳ����û�ҵ���Ӧ�ĻỰ��
		String gowhich="info_ListShow.action?infoType="+infoType;	//������Ϣ����ת����
		//����OpDB���е�OpCreatePage()����������ܼ�¼������ҳ�����������õ�ǰҳ�룬��Щ��Ϣ����װ����createPage������
		CreatePage createPage=myOp.OpCreatePage(sql_free, params,perR,strCurrentP,gowhich);		
		//��ʼ��ҳ����Ϣ
		int top1 = createPage.getPerR();		//ÿҳ�����ʾ��
		int currentP = createPage.getCurrentP();		//��ǰҳ

		if(currentP==1){
			sqlFreeSub = "select * from tb_info where (info_type=?) and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc limit " + top1;
		}else{
			int top2 = (currentP-1)*top1;
			sqlFreeSub = "select * from tb_info where (info_type=?) and (info_state = '1' ) and (info_payfor = '0') "
					+ "and (info_date < (select MIN(info_date) from (select * from tb_info where (info_type='1') "
							+ "and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc limit "+top2 + ") as mindate)) order by info_date desc limit "+top1 ;
		}
		List onefreelist=myOp.OpListShow(sqlFreeSub, params);		//��ȡ��ѯ���Ľ����
		request.setAttribute("onefreelist", onefreelist);			
		request.setAttribute("createPage", createPage);
		
		return SUCCESS;
	}
	//������Ϣ�ľ���չʾ
	public String SingleShow(){
		request.setAttribute("mainPage","/pages/show/singleshow.jsp");
		//��ȡ��ϢID
		String id = request.getParameter("id");
		String sql="select * from tb_info where id="+id;
		OpDB myOp = new OpDB();
		infoSingle = myOp.OpSingleShow(sql, null);
		if(infoSingle==null){
			request.setAttribute("mainPage", "/pages/error.jsp");
			addFieldError("SingleShowNoExist",getText("��Ϣ������"));
		}
		return SUCCESS;
	}
	//��֤����Ϣ���ڽ��뷢����Ϣ�ı�������Ѿ����ø÷�����
	public void validateAdd(){	
		//System.out.println("����ִ��validateAdd()������");
		request.setAttribute("mainPage","/pages/add/addInfo.jsp");
	
		String addType=request.getParameter("addType");	
		if(addType==null||addType.equals(""))
			addType="linkTo";
		
		if(addType.equals("add")){			
			int type=infoSingle.getInfoType();
			String title=infoSingle.getInfoTitle();
			String content=infoSingle.getInfoContent();
			String phone=infoSingle.getInfoPhone();
			String linkman=infoSingle.getInfoLinkman();
			String email=infoSingle.getInfoEmail();			
			
			boolean mark=true;			
			if(type<=0){
				mark=false;
				addFieldError("typeError",getText("û������"));								
			}
			if(title==null||title.equals("")){
				mark=false;
				addFieldError("titleError",getText("û�б���"));
			}
			if(content==null||content.equals("")){
				mark=false;
				addFieldError("contentError",getText("û������"));
			}
			if(phone==null||phone.equals("")){
				mark=false;
				addFieldError("phoneError",getText("û���ֻ�"));
			}
			if(linkman==null||linkman.equals("")){
				mark=false;
				addFieldError("linkmanError",getText("û����ϵ��"));
			}
			if(email==null||email.equals("")){
				mark=false;
				addFieldError("emailError",getText("û��email"));
			}
			if(mark){
				String phoneRegex="(\\d{3}-)\\d{8}|(\\d{4}-)(\\d{7}|\\d{8})|\\d{11}";
				if(phone.indexOf(",")<0){
					if(!infoSingle.getInfoPhone().matches(phoneRegex)){
						addFieldError("phoneError",getText("�ֻ�����ȷ"));
					}					
				}
				else{
					String[] phones=phone.split(",");
					for(int i=0;i<phones.length;i++){
						if(!phones[i].matches(phoneRegex)){							
							addFieldError("phoneError",getText("�ֻ��Ÿ�ʽ����"));							
							break;
						}
					}
				}
				String emailRegex="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
				if(!infoSingle.getInfoEmail().matches(emailRegex)){
					addFieldError("emailError",getText("�����ַ����"));
				}				
			}
		}
	}
	//��ӱ���Ϣ
	public String Add(){
		//System.out.println("����ִ��Add()������");
		String addType=request.getParameter("addType");
		if(addType==null||addType.equals("")){
			request.setAttribute("mainPage","/pages/add/addInfo.jsp");
			addType="linkTo";
		}
		if(addType.equals("add")){
			request.setAttribute("mainPage","/pages/error.jsp");
			OpDB myOp=new OpDB();			
			Integer type=Integer.valueOf(infoSingle.getInfoType());
			String	title=infoSingle.getInfoTitle();
			String	content=DoString.HTMLChange(infoSingle.getInfoContent());
			String	phone=infoSingle.getInfoPhone();
			phone=phone.replaceAll(",","��");
			
			//��ȡ��ǰ��¼��
			DB db = new DB();
			//System.out.println(db.sum());
			String id = db.sum()+ 100 +"";
			db.closed();
			String linkman=infoSingle.getInfoLinkman();
			String email=infoSingle.getInfoEmail();
			String date=DoString.dateTimeChange(new java.util.Date());			
			String state="0";
			String payfor="0";
			
			Object[] params={id,type,title,content,linkman,phone,email,date,state,payfor};
			String sql="insert into tb_info values(?,?,?,?,?,?,?,?,?,?)";
			
			int i=myOp.OpUpdate(sql,params);			
			if(i<=0)	//����ʧ��
				addFieldError("addE",getText("���ʧ��"));				
			else{
				sql="select * from tb_info where info_date=?";
				Object[] params1={date};				
				int infoNum=myOp.OpSingleShow(sql, params1).getId();				
				addFieldError("addS",getText("��ӳɹ��� ")+infoNum);				
			}
		}		
		return SUCCESS;
	}
	
	//��������
	public String SearchShow() throws UnsupportedEncodingException{
		request.setAttribute("mainPage","/pages/show/searchshow.jsp");		
		
		String subsql=searchInfo.getSubsql();
		String sqlvalue=searchInfo.getSqlvalue();		
		String type=searchInfo.getType();
		
		String showType=request.getParameter("showType");
		if(showType==null)
			showType="";
		if(showType.equals("link")){		//�Դӳ������л�ȡ�Ĳ�������ת�����
			try {
				sqlvalue=new String(sqlvalue.getBytes("ISO-8859-1"),"gb2312");
			} catch (UnsupportedEncodingException e) {			
				sqlvalue="";
				e.printStackTrace();
			}
			searchInfo.setSqlvalue(sqlvalue);
		}
		
		session.put("subsql",subsql);
		session.put("sqlvalue",sqlvalue);
		session.put("type",type);
		
		String param="";
		String opname="";
		if(type.equals("like")){
			opname=" LIKE ";
			param="%"+sqlvalue+"%";			
		}
		else{		
			opname=" = ";
			param=sqlvalue;			
		}
		
		String sqlSearchAll="SELECT * FROM tb_info WHERE ("+subsql+opname+"?) ORDER BY info_date DESC";
		String sqlSearchSub="";		
		Object[] params={param};		

		int perR=8;
		String strCurrentP=request.getParameter("showpage");
		String gowhich = "info_SearchShow.action?searchInfo.subsql="+subsql+"&searchInfo.sqlvalue="+sqlvalue+"&searchInfo.type="+type+"&showType=link";
		
		OpDB myOp=new OpDB();
		CreatePage createPage=myOp.OpCreatePage(sqlSearchAll, params,perR,strCurrentP,gowhich);			//����OpDB���е�OpCreatePage()����������ܼ�¼������ҳ�����������õ�ǰҳ�룬��Щ��Ϣ����װ����createPage������
		
		int top1=createPage.getPerR();
		int currentP=createPage.getCurrentP();
		
		if(currentP==1){     		//��ʾ��1ҳ��Ϣ��SQL���
			sqlSearchSub="SELECT * FROM tb_info WHERE ("+subsql+opname+"?) ORDER BY info_date DESC limit "+ top1;
		}
		else{						//��ʾ����1ҳ�⣬����ָ��ҳ����Ϣ��SQl���
			int top2=(currentP-1)*top1;
			sqlSearchSub="SELECT * FROM tb_info WHERE ("+subsql+opname+"?) AND (info_date < (SELECT MIN(info_date) FROM (SELECT info_date FROM tb_info WHERE "+subsql+opname+"'"+param+"' ORDER BY info_date DESC limit" + top2 +") AS mindate)) ORDER BY info_date DESC limit " + top1;
//			sqlSearchSub="SELECT TOP "+top1+" * FROM tb_info WHERE ("+subsql+opname+"?) AND (info_date NOT IN (SELECT TOP "+top2+" info_date FROM tb_info WHERE "+subsql+opname+"'"+param+"' ORDER BY info_date DESC)) ORDER BY info_date DESC";				//��һ��ʵ�ַ�ҳ��ѯ��SQL���
		}
		
		List searchlist=myOp.OpListShow(sqlSearchSub, params);
		request.setAttribute("searchlist",searchlist);
		request.setAttribute("createpage", createPage);
		
		return SUCCESS;
	}
	
	//����������֤
	public void validateSearchShow() {
		request.setAttribute("mainPage","/pages/error.jsp");		
		String subsql=searchInfo.getSubsql();
		String sqlvalue=searchInfo.getSqlvalue();
		String type=searchInfo.getType();
		
		if(subsql==null||subsql.equals("")){
			addFieldError("SearchNoC",getText("û�з�����Ϣ"));
		}
		if(sqlvalue==null||sqlvalue.equals("")){
			addFieldError("SearchNoV",getText("û�з�����Ϣ"));
		}
		if(type==null||type.equals("")){
			addFieldError("SearchNoT",getText("û�з�����Ϣ"));
		}
	}
	
}
