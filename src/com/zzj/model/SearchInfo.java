package com.zzj.model;

public class SearchInfo {
	private String subsql;		//left.jsp�������б��ѡ��
	private String sqlvalue;	//�û�����Ĺؼ���
	private String type="all";	//�Ƿ�ȫ��ƥ�� or ģ������
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
