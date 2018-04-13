<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
  //String path = request.getContextPath();
  //String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

  String mainPage=(String)request.getAttribute("mainPage");
  if(mainPage==null||mainPage.equals(""))
	  mainPage="default.jsp";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>都市信息网</title>
<style type="text/css">
td {
	font-size: 9pt;	color: #000000;
}

a:hover {
	font-size: 9pt;	color: #FF0000;
	text-decoration:underline
}
a {
	font-size: 9pt;	text-decoration: none;	color: black;
	noline:expression(this.onfocus=this.blur);
}
input{
	font-family: "宋体";
	font-size: 9pt;
	color: #333333;

}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	
}
.textarea {
	font-family: "宋体";
	font-size: 9pt;
	color: #333333;
	border: 1px solid #999999;
}
</style>
</head>
<body background="images/back.gif">
	<center>
        <table border="0" width="920" cellspacing="0" cellpadding="0" bgcolor="white">
            <tr><td colspan="2"><jsp:include page="top.jsp"/></td></tr>
            <tr>
                <td width="230" valign="top" align="center"><jsp:include page="left.jsp"/></td>
                <td width="690" height="400" align="center" valign="top" bgcolor="#FFFFFF"><jsp:include page="<%=mainPage%>"/></td>
            </tr>
            <tr><td colspan="2"><%@ include file="end.jsp" %></td></tr>
        </table>        
    </center>
</body>
</html>