package com.zzj.action;

import java.util.*;

import com.zzj.actionSuper.*;
import com.zzj.dao.*;

public class IndexAction extends MySuperAction{
	public static TreeMap typeMap;				//��������б�		
	public static TreeMap searchMap;			//���������е������б�
	
	public String execute() throws Exception {
		/* ��ѯ�����շ���Ϣ��������ʱ�併������ */
		OpDB myOp=new OpDB();
		String sql1="select * from tb_info where (info_state='1') and (info_payfor = '1') order by info_date desc";
		List payforlist=myOp.OpListShow(sql1,null);
		request.setAttribute("payforlist",payforlist);		

		/* ��ѯ�����Ϣ��������ʱ�併������ */
		List allsublist=new ArrayList();
		if(typeMap!=null&&typeMap.size()!=0){
			Iterator itype=typeMap.keySet().iterator();			
			String sql2="SELECT * FROM tb_info WHERE (info_type = ?) AND (info_state='1') AND (info_payfor = '0') ORDER BY info_date DESC";			
			while(itype.hasNext()){
				Integer sign=(Integer)itype.next();
				Object[] params={sign};
				List onesublist=myOp.OpListShow(sql2, params);
				if(!onesublist.isEmpty()){
					//System.out.println("�ǿ�");
					allsublist.add(onesublist);	
				}			
			}
			System.out.println(allsublist.size());
		}
		request.setAttribute("allsublist",allsublist);
		session.put("typeMap",typeMap);
		session.put("searchMap",searchMap);		
		
		return SUCCESS;
	}
	
	static{
		OpDB myOp=new OpDB();
		
		/* ��ȡ������Ϣ��� */
		String sql="select * from tb_type order by type_sign";
		typeMap=myOp.OpGetListBox(sql,null);
		if(typeMap==null)
			typeMap=new TreeMap();
		/* ��ʼ���������ܵ������б� */
		searchMap=new TreeMap();
		searchMap.put("IDֵ","id");
		searchMap.put("��Ϣ����","info_title");
		searchMap.put("��Ϣ����","info_content");
		searchMap.put("��ϵ��","info_linkman");
		searchMap.put("��ϵ�绰","info_phone");
		searchMap.put("E-mail��ַ","info_email");
	}
}
