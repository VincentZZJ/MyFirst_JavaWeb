package com.zzj.dao;

import java.sql.*;
import java.util.*;

import com.zzj.model.*;
import com.zzj.tools.*;

public class OpDB {
	private DB mydb;
	//初始化构造函数
	public OpDB(){
		mydb = new DB();
	}
	/** 用于前台及导航栏的信息处理--（等会调用时再返回来看）  **/
	public TreeMap OpGetListBox(String sql,Object[] params){
		TreeMap typeMap=new TreeMap();
		mydb.doPstm(sql, params);
		try {
			ResultSet rs=mydb.getRS();
			if(rs!=null){
				while(rs.next()){					
					Integer sign=Integer.valueOf(rs.getInt("type_sign"));
					String intro=rs.getString("type_intro");
					typeMap.put(sign,intro);					
				}
				rs.close();
			}
		} catch (SQLException e) {			
			System.out.println("OpGetListBox()方法查询失败！");			
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return typeMap;	
	}
	
	//用于获取查询符合的信息
	public List OpListShow(String sql,Object[] params){
		List onelist=new ArrayList();
		mydb.doPstm(sql, params);
		try{
			ResultSet rs=mydb.getRS();
			//int i=0;
			if(rs!=null){
				while(rs.next()){
					//i++;
					InfoSingle infoSingle=new InfoSingle();
					infoSingle.setId(rs.getInt("id"));
					infoSingle.setInfoType(rs.getInt("info_type"));
					infoSingle.setInfoTitle(rs.getString("info_title"));
					infoSingle.setInfoContent(rs.getString("info_content"));
					infoSingle.setInfoLinkman(rs.getString("info_linkman"));
					infoSingle.setInfoPhone(rs.getString("info_phone"));
					infoSingle.setInfoEmail(rs.getString("info_email"));
					infoSingle.setInfoDate(DoString.dateTimeChange(rs.getTimestamp("info_date")));
					infoSingle.setInfoState(rs.getString("info_state"));
					infoSingle.setInfoPayfor(rs.getString("info_payfor"));
					//System.out.println(rs.getString("info_title" + " " + rs.getString("info_payfor")));
					onelist.add(infoSingle);					
				}
				//System.out.println(i);
			}
			rs.close();
		}catch (Exception e){
			System.out.println("查看信息列表失败！(查询数据库)");			
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return onelist;		
	}
	
	/** 用于获取信息的详细内容--（等会调用时再返回来看）  **/
	public InfoSingle OpSingleShow(String sql,Object[] params){
		InfoSingle infoSingle=null;
		mydb.doPstm(sql, params);       
		try{
		    ResultSet rs=mydb.getRS();
			if(rs!=null&&rs.next()){
				infoSingle=new InfoSingle();
				infoSingle.setId(rs.getInt("id"));
				infoSingle.setInfoType(rs.getInt("info_type"));
				infoSingle.setInfoTitle(rs.getString("info_title"));
				infoSingle.setInfoContent(rs.getString("info_content"));
				infoSingle.setInfoLinkman(rs.getString("info_linkman"));
				infoSingle.setInfoPhone(rs.getString("info_phone"));
				infoSingle.setInfoEmail(rs.getString("info_email"));
				infoSingle.setInfoDate(DoString.dateTimeChange(rs.getTimestamp("info_date")));
				infoSingle.setInfoState(rs.getString("info_state"));
				infoSingle.setInfoPayfor(rs.getString("info_payfor"));				
				rs.close();				
			}
		}catch(Exception e){
			System.out.println("查看详细内容失败！(查询数据库)");
			e.printStackTrace();			
		}finally{
			mydb.closed();
		}
		return infoSingle;
	}
	
	/** 更新数据库操作--（等会调用时再返回来看）  **/
	public int OpUpdate(String sql,Object[] params){		
		int i=-1;
		mydb.doPstm(sql, params);
		try{
			i=mydb.getCount();			
		}catch(SQLException e){
			System.out.println("执行OpUpdate()方法失败！(更新数据库)");
			e.printStackTrace();
		}finally{
			mydb.closed();
		}
		return i;
	}
	
	/** 信息分页操作--（等会调用时再返回来看）  **/
	public CreatePage OpCreatePage(String sqlall,Object[] params,int perR,String strCurrentP,String gowhich){
		CreatePage page=new CreatePage();
		page.setPerR(perR);
		if(sqlall!=null&&!sqlall.equals("")){
			DB mydb=new DB();
			mydb.doPstm(sqlall,params);			
			try {
				//System.out.println("已进入");
				ResultSet rs=mydb.getRS();				
				if(rs!=null&&rs.next()){
					rs.last();					
					page.setAllR(rs.getRow());			//设置总记录数
					page.setAllP();						//设置总页数
					page.setCurrentPage(strCurrentP);	//当前页数
					page.setPageInfo();
					page.setPageLink(gowhich);			//设置信息的跳转链接
					rs.close();
					//System.out.println(page.getPageInfo());
				}
			} catch (SQLException e) {
				System.out.println("OpDB.java/OpCreatePage()方法：创建CreatePage分页类失败！");
				e.printStackTrace();
			}finally{				
				mydb.closed();
			}
		}		
		return page;
	}
	//删除操作时更新库中id值
	public void OpUpdateId(String deleteID){
		String sql = "update tb_info set id=id-1 where id>" + deleteID;
		int i = mydb.updateID(sql);
		if(i>0){
			System.out.println("更新ID成功！");
		}else{
			System.out.println("更新ID失败！");
		}
	}
	
	//登录操作的验证
	public boolean LogOn(String sql,Object[] params){
		mydb.doPstm(sql, params);
		try {
			ResultSet rs=mydb.getRS();
			boolean mark=(rs==null||!rs.next()?false:true);
			rs.close();
			return mark;			
		} catch (SQLException e) {
			System.out.println("登录失败！");
			e.printStackTrace();
			return false;
		}
		finally{
			mydb.closed();
		}
	}
}
