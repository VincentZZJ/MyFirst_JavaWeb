package com.zzj.actionSuper;

import com.zzj.model.*;

public class InfoSuperAction extends MySuperAction{
	protected InfoSingle infoSingle;		//������Ϣ�Ķ���
	protected SearchInfo searchInfo;		//ÿ��������Ҫ����Ϣ
	public InfoSingle getInfoSingle() {
		return infoSingle;
	}
	public void setInfoSingle(InfoSingle infoSingle) {
		this.infoSingle = infoSingle;
	}
	public SearchInfo getSearchInfo() {
		return searchInfo;
	}
	public void setSearchInfo(SearchInfo searchInfo) {
		this.searchInfo = searchInfo;
	}

}
