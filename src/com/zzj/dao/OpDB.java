package com.zzj.dao;

import java.sql.*;
import java.util.*;

import com.zzj.model.*;
import com.zzj.tools.*;

public class OpDB {
	private DB mydb;
	//��ʼ�����캯��
	public OpDB(){
		mydb = new DB();
	}
	/** ����ǰ̨������������Ϣ����--���Ȼ����ʱ�ٷ���������  **/
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
			System.out.println("OpGetListBox()������ѯʧ�ܣ�");			
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return typeMap;	
	}
	
	//���ڻ�ȡ��ѯ���ϵ���Ϣ
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
			System.out.println("�鿴��Ϣ�б�ʧ�ܣ�(��ѯ���ݿ�)");			
			e.printStackTrace();
		}finally{
			mydb.closed();			
		}
		return onelist;		
	}
	
	/** ���ڻ�ȡ��Ϣ����ϸ����--���Ȼ����ʱ�ٷ���������  **/
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
			System.out.println("�鿴��ϸ����ʧ�ܣ�(��ѯ���ݿ�)");
			e.printStackTrace();			
		}finally{
			mydb.closed();
		}
		return infoSingle;
	}
	
	/** �������ݿ����--���Ȼ����ʱ�ٷ���������  **/
	public int OpUpdate(String sql,Object[] params){		
		int i=-1;
		mydb.doPstm(sql, params);
		try{
			i=mydb.getCount();			
		}catch(SQLException e){
			System.out.println("ִ��OpUpdate()����ʧ�ܣ�(�������ݿ�)");
			e.printStackTrace();
		}finally{
			mydb.closed();
		}
		return i;
	}
	
	/** ��Ϣ��ҳ����--���Ȼ����ʱ�ٷ���������  **/
	public CreatePage OpCreatePage(String sqlall,Object[] params,int perR,String strCurrentP,String gowhich){
		CreatePage page=new CreatePage();
		page.setPerR(perR);
		if(sqlall!=null&&!sqlall.equals("")){
			DB mydb=new DB();
			mydb.doPstm(sqlall,params);			
			try {
				//System.out.println("�ѽ���");
				ResultSet rs=mydb.getRS();				
				if(rs!=null&&rs.next()){
					rs.last();					
					page.setAllR(rs.getRow());			//�����ܼ�¼��
					page.setAllP();						//������ҳ��
					page.setCurrentPage(strCurrentP);	//��ǰҳ��
					page.setPageInfo();
					page.setPageLink(gowhich);			//������Ϣ����ת����
					rs.close();
					//System.out.println(page.getPageInfo());
				}
			} catch (SQLException e) {
				System.out.println("OpDB.java/OpCreatePage()����������CreatePage��ҳ��ʧ�ܣ�");
				e.printStackTrace();
			}finally{				
				mydb.closed();
			}
		}		
		return page;
	}
	//ɾ������ʱ���¿���idֵ
	public void OpUpdateId(String deleteID){
		String sql = "update tb_info set id=id-1 where id>" + deleteID;
		int i = mydb.updateID(sql);
		if(i>0){
			System.out.println("����ID�ɹ���");
		}else{
			System.out.println("����IDʧ�ܣ�");
		}
	}
	
	//��¼��������֤
	public boolean LogOn(String sql,Object[] params){
		mydb.doPstm(sql, params);
		try {
			ResultSet rs=mydb.getRS();
			boolean mark=(rs==null||!rs.next()?false:true);
			rs.close();
			return mark;			
		} catch (SQLException e) {
			System.out.println("��¼ʧ�ܣ�");
			e.printStackTrace();
			return false;
		}
		finally{
			mydb.closed();
		}
	}
}
