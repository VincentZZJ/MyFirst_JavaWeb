package com.zzj.dao;

import java.sql.*;

public class DB {
	private Connection con ;
	private PreparedStatement pstm;
	private String username = "root";
	private String password = "123456";
	private String className = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/t19.1?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	
	//��ʼ������������������
	public DB(){
		try{
			Class.forName(className);
		}catch(ClassNotFoundException ex){
			System.out.println("��������ʧ�ܣ�");
			ex.printStackTrace();
		}
	}
	//�������ݿ����ӽӿ�Con
	public Connection getCon(){
		try{
			con = DriverManager.getConnection(url, username,password);
		}catch (SQLException e){
			System.out.println("�������ݿ�����ʧ�ܣ�");
			con = null;
			e.printStackTrace();
		}
		return con;
	}
	//ɾ�������IDֵ
	public int updateID(String sql){
		int i=-1;
		this.getCon();
		Statement stmt = null;
		if(con!=null){
			try{
				stmt = con.createStatement();
				stmt.executeUpdate(sql);
				i = stmt.getUpdateCount();
				System.out.println(" " + i);
			}catch (SQLException e){
				System.out.println("updateID���������ˣ�");
				e.printStackTrace();
			}
		}
		return i;
	}
	//��ȡ��ǰ��¼��
	public int sum(){
		int len = 0;
		String sql = "select count(*) from tb_info";
		Statement stmt = null;
		ResultSet rs = null;
		this.getCon();
		if(con!=null){
			try{
				System.out.println(sql);
				stmt = con.createStatement();
				//System.out.println("1");
				rs = stmt.executeQuery(sql);
				//System.out.println("2");
				rs.next();
				len = rs.getInt(1);
				System.out.println(len + "");
				//System.out.println("3");
				len = len + 1;
				//System.out.println(len + "");
				//this.closed();
			}catch (SQLException e){
				System.out.println("sum���������ˣ�");
				e.printStackTrace();
			}
		}
		return len;
	}
	
	//�����ݽ��в���
	public void doPstm(String sql,Object[] params){
		if(sql!=null || !sql.equals("")){
			if(params==null){
				params = new Object[0];
			}
			this.getCon();
			if(con!=null){
				try{
					System.out.println(sql);
					pstm = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					for(int i=0;i<params.length;i++){
						pstm.setObject(i+1,params[i]);			//��1��ʼ
					}
					pstm.execute();			//ִ��sql���
				}catch (SQLException e){
					System.out.println("doPstm()���������ˣ�");
					e.printStackTrace();
				}
			}
		}
	}
	//��doPstm()��ִ��SQLָ���û�л�ȡ��RS��������ʴ����÷���
	//---------ִ���˲�ѯ��SQL��䷵�صĽ����--------
	public ResultSet getRS() throws SQLException{
		return pstm.getResultSet();
	}
	//---------ִ���˸�����SQL��䷵�ص���Ӱ��ļ�¼��--------
	public int getCount() throws SQLException{
		return pstm.getUpdateCount();
	}
	//�ر����ݿ�����
	public void closed(){
		try{
			if(pstm!=null){
				pstm.close();
			}
		}catch (SQLException e){
			System.out.println("�ر�pstmʧ�ܣ�");
			e.printStackTrace();
		}
		try{
			if(con!=null){
				con.close();
			}
		}catch (SQLException e){
			System.out.println("�ر�conʧ�ܣ�");
			e.printStackTrace();
		}
	}
}
