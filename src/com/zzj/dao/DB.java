package com.zzj.dao;

import java.sql.*;

public class DB {
	private Connection con ;
	private PreparedStatement pstm;
	private String username = "root";
	private String password = "123456";
	private String className = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/t19.1?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	
	//初始化函数（加载驱动）
	public DB(){
		try{
			Class.forName(className);
		}catch(ClassNotFoundException ex){
			System.out.println("加载驱动失败！");
			ex.printStackTrace();
		}
	}
	//创建数据库连接接口Con
	public Connection getCon(){
		try{
			con = DriverManager.getConnection(url, username,password);
		}catch (SQLException e){
			System.out.println("创建数据库连接失败！");
			con = null;
			e.printStackTrace();
		}
		return con;
	}
	//删除后更新ID值
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
				System.out.println("updateID方法出错了！");
				e.printStackTrace();
			}
		}
		return i;
	}
	//获取当前记录数
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
				System.out.println("sum方法出错了！");
				e.printStackTrace();
			}
		}
		return len;
	}
	
	//对数据进行操作
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
						pstm.setObject(i+1,params[i]);			//从1开始
					}
					pstm.execute();			//执行sql语句
				}catch (SQLException e){
					System.out.println("doPstm()方法出错了！");
					e.printStackTrace();
				}
			}
		}
	}
	//在doPstm()中执行SQL指令后并没有获取到RS结果集，故创建该方法
	//---------执行了查询类SQL语句返回的结果集--------
	public ResultSet getRS() throws SQLException{
		return pstm.getResultSet();
	}
	//---------执行了更新类SQL语句返回的所影响的记录数--------
	public int getCount() throws SQLException{
		return pstm.getUpdateCount();
	}
	//关闭数据库连接
	public void closed(){
		try{
			if(pstm!=null){
				pstm.close();
			}
		}catch (SQLException e){
			System.out.println("关闭pstm失败！");
			e.printStackTrace();
		}
		try{
			if(con!=null){
				con.close();
			}
		}catch (SQLException e){
			System.out.println("关闭con失败！");
			e.printStackTrace();
		}
	}
}
