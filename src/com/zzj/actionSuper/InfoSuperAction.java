package com.zzj.actionSuper;

import com.zzj.model.*;

public class InfoSuperAction extends MySuperAction{
	protected InfoSingle infoSingle;		//单条信息的对象
	protected SearchInfo searchInfo;		//每次搜索的要求信息
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
