package com.zzj.action;

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.zzj.actionSuper.*;
import com.zzj.dao.*;
import com.zzj.model.*;
import com.zzj.tools.*;

public class InfoAction extends InfoSuperAction{
	public String ListShow(){
		//此处在请求中设置要跳转的页面
		request.setAttribute("mainPage", "/pages/show/listshow.jsp");
		//获取信息的类别
		String infoType = request.getParameter("infoType");
		System.out.println(infoType);
		Object[] params = {infoType};
		String sql_payfor = "select * from tb_info where (info_type = ? ) and (info_state = '1' ) and (info_payfor = '1' ) order by info_date desc";
		//链接数据库
		OpDB myOp = new OpDB();
		//获取所有付费信息
		List onepayforlist = myOp.OpListShow(sql_payfor, params);
		request.setAttribute("onepayforlist", onepayforlist);
		//获取所有免费信息
		String sql_free = "select * from tb_info where (info_type = ? ) and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc";
		String sqlFreeSub="";
		int perR=3;			//每页能显示的信息条数
		String strCurrentP=request.getParameter("showpage");		//当前页数（没找到对应的会话）
		String gowhich="info_ListShow.action?infoType="+infoType;	//设置信息的跳转链接
		//调用OpDB类中的OpCreatePage()方法计算出总记录数、总页数，并且设置当前页码，这些信息都封装到了createPage对象中
		CreatePage createPage=myOp.OpCreatePage(sql_free, params,perR,strCurrentP,gowhich);		
		//初始化页码信息
		int top1 = createPage.getPerR();		//每页最大显示数
		int currentP = createPage.getCurrentP();		//当前页

		if(currentP==1){
			sqlFreeSub = "select * from tb_info where (info_type=?) and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc limit " + top1;
		}else{
			int top2 = (currentP-1)*top1;
			sqlFreeSub = "select * from tb_info where (info_type=?) and (info_state = '1' ) and (info_payfor = '0') "
					+ "and (info_date < (select MIN(info_date) from (select * from tb_info where (info_type='1') "
							+ "and (info_state = '1' ) and (info_payfor = '0' ) order by info_date desc limit "+top2 + ") as mindate)) order by info_date desc limit "+top1 ;
		}
		List onefreelist=myOp.OpListShow(sqlFreeSub, params);		//获取查询到的结果集
		request.setAttribute("onefreelist", onefreelist);			
		request.setAttribute("createPage", createPage);
		
		return SUCCESS;
	}
	//单条信息的具体展示
	public String SingleShow(){
		request.setAttribute("mainPage","/pages/show/singleshow.jsp");
		//获取信息ID
		String id = request.getParameter("id");
		String sql="select * from tb_info where id="+id;
		OpDB myOp = new OpDB();
		infoSingle = myOp.OpSingleShow(sql, null);
		if(infoSingle==null){
			request.setAttribute("mainPage", "/pages/error.jsp");
			addFieldError("SingleShowNoExist",getText("信息不存在"));
		}
		return SUCCESS;
	}
	//验证表单信息（在进入发布信息的表单界面就已经调用该方法）
	public void validateAdd(){	
		//System.out.println("正在执行validateAdd()方法…");
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
				addFieldError("typeError",getText("没有类型"));								
			}
			if(title==null||title.equals("")){
				mark=false;
				addFieldError("titleError",getText("没有标题"));
			}
			if(content==null||content.equals("")){
				mark=false;
				addFieldError("contentError",getText("没有内容"));
			}
			if(phone==null||phone.equals("")){
				mark=false;
				addFieldError("phoneError",getText("没有手机"));
			}
			if(linkman==null||linkman.equals("")){
				mark=false;
				addFieldError("linkmanError",getText("没有联系人"));
			}
			if(email==null||email.equals("")){
				mark=false;
				addFieldError("emailError",getText("没有email"));
			}
			if(mark){
				String phoneRegex="(\\d{3}-)\\d{8}|(\\d{4}-)(\\d{7}|\\d{8})|\\d{11}";
				if(phone.indexOf(",")<0){
					if(!infoSingle.getInfoPhone().matches(phoneRegex)){
						addFieldError("phoneError",getText("手机不正确"));
					}					
				}
				else{
					String[] phones=phone.split(",");
					for(int i=0;i<phones.length;i++){
						if(!phones[i].matches(phoneRegex)){							
							addFieldError("phoneError",getText("手机号格式错误"));							
							break;
						}
					}
				}
				String emailRegex="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
				if(!infoSingle.getInfoEmail().matches(emailRegex)){
					addFieldError("emailError",getText("邮箱地址错误"));
				}				
			}
		}
	}
	//添加表单信息
	public String Add(){
		//System.out.println("正在执行Add()方法…");
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
			phone=phone.replaceAll(",","●");
			
			//获取当前记录数
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
			if(i<=0)	//插入失败
				addFieldError("addE",getText("添加失败"));				
			else{
				sql="select * from tb_info where info_date=?";
				Object[] params1={date};				
				int infoNum=myOp.OpSingleShow(sql, params1).getId();				
				addFieldError("addS",getText("添加成功： ")+infoNum);				
			}
		}		
		return SUCCESS;
	}
	
	//搜索操作
	public String SearchShow() throws UnsupportedEncodingException{
		request.setAttribute("mainPage","/pages/show/searchshow.jsp");		
		
		String subsql=searchInfo.getSubsql();
		String sqlvalue=searchInfo.getSqlvalue();		
		String type=searchInfo.getType();
		
		String showType=request.getParameter("showType");
		if(showType==null)
			showType="";
		if(showType.equals("link")){		//对从超链接中获取的参数进行转码操作
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
		CreatePage createPage=myOp.OpCreatePage(sqlSearchAll, params,perR,strCurrentP,gowhich);			//调用OpDB类中的OpCreatePage()方法计算出总记录数、总页数，并且设置当前页码，这些信息都封装到了createPage对象中
		
		int top1=createPage.getPerR();
		int currentP=createPage.getCurrentP();
		
		if(currentP==1){     		//显示第1页信息的SQL语句
			sqlSearchSub="SELECT * FROM tb_info WHERE ("+subsql+opname+"?) ORDER BY info_date DESC limit "+ top1;
		}
		else{						//显示除第1页外，其他指定页码信息的SQl语句
			int top2=(currentP-1)*top1;
			sqlSearchSub="SELECT * FROM tb_info WHERE ("+subsql+opname+"?) AND (info_date < (SELECT MIN(info_date) FROM (SELECT info_date FROM tb_info WHERE "+subsql+opname+"'"+param+"' ORDER BY info_date DESC limit" + top2 +") AS mindate)) ORDER BY info_date DESC limit " + top1;
//			sqlSearchSub="SELECT TOP "+top1+" * FROM tb_info WHERE ("+subsql+opname+"?) AND (info_date NOT IN (SELECT TOP "+top2+" info_date FROM tb_info WHERE "+subsql+opname+"'"+param+"' ORDER BY info_date DESC)) ORDER BY info_date DESC";				//另一种实现分页查询的SQL语句
		}
		
		List searchlist=myOp.OpListShow(sqlSearchSub, params);
		request.setAttribute("searchlist",searchlist);
		request.setAttribute("createpage", createPage);
		
		return SUCCESS;
	}
	
	//搜索表单的验证
	public void validateSearchShow() {
		request.setAttribute("mainPage","/pages/error.jsp");		
		String subsql=searchInfo.getSubsql();
		String sqlvalue=searchInfo.getSqlvalue();
		String type=searchInfo.getType();
		
		if(subsql==null||subsql.equals("")){
			addFieldError("SearchNoC",getText("没有符合信息"));
		}
		if(sqlvalue==null||sqlvalue.equals("")){
			addFieldError("SearchNoV",getText("没有符合信息"));
		}
		if(type==null||type.equals("")){
			addFieldError("SearchNoT",getText("没有符合信息"));
		}
	}
	
}
