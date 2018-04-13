package com.zzj.model;

public class SearchInfo {
	private String subsql;		//left.jsp中下拉列表的选项
	private String sqlvalue;	//用户输入的关键字
	private String type="all";	//是否全字匹配 or 模糊搜索
	public String getSubsql() {
		return subsql;
	}
	public void setSubsql(String subsql) {
		this.subsql = subsql;
	}
	public String getSqlvalue() {
		return sqlvalue;
	}
	public void setSqlvalue(String sqlvalue) {
		this.sqlvalue = sqlvalue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
