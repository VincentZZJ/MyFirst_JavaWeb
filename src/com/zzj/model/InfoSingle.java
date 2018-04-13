package com.zzj.model;

public class InfoSingle {
	private int id;						//��ϢID
	private int infoType;				//��Ϣ���
	private String infoTitle;			//��Ϣ����
	private String infoContent;			//��Ϣ����
	private String infoLinkman;			//��Ϣ��ϵ��
	private String infoPhone;			//��ϵ�绰
	private String infoEmail;			//��Ϣ�ʼ���ַ
	private String infoDate; 			//��Ϣ����ʱ��
	private String infoState;			//��Ϣ���״̬
	private String infoPayfor;			//��Ϣ����״̬
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}
	public String getInfoTitle() {
		return infoTitle;
	}
	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public String getInfoLinkman() {
		return infoLinkman;
	}
	public void setInfoLinkman(String infoLinkman) {
		this.infoLinkman = infoLinkman;
	}
	public String getInfoPhone() {
		return infoPhone;
	}
	public void setInfoPhone(String infoPhone) {
		this.infoPhone = infoPhone;
	}
	public String getInfoEmail() {
		return infoEmail;
	}
	public void setInfoEmail(String infoEmail) {
		this.infoEmail = infoEmail;
	}
	public String getInfoDate() {
		return infoDate;
	}
	public void setInfoDate(String infoDate) {
		this.infoDate = infoDate;
	}
	public String getInfoState() {
		return infoState;
	}
	public void setInfoState(String infoState) {
		this.infoState = infoState;
	}
	public String getInfoPayfor() {
		return infoPayfor;
	}
	public void setInfoPayfor(String infoPayfor) {
		this.infoPayfor = infoPayfor;
	}
	//�����Ϣ������߳����⣬������ʾ
	public String getSubInfoTitle(int Len){
		if(Len<=0||Len>=infoTitle.length()){
			Len = infoTitle.length();
		}
		return this.infoTitle.substring(0, Len);
	}
}
