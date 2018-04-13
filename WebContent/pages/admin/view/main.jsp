<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s2" uri="/struts-tags" %>
<%
  String mainPage=(String)request.getAttribute("mainPage");
  if(mainPage==null||mainPage.equals(""))
	  mainPage="default.jsp";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="<s2:url value='css/style.css'/>">
</head>
<body>
<center>
        <table border="0" cellpadding="0" cellspacing="0" width="688" height="100%">
            <tr height="20"><td><img src="images/default_t.jpg"></td></tr>
            <tr><td background="images/default_m.jpg" valign="top"><jsp:include page="<%=mainPage%>"/></td></tr>
            <tr height="26"><td><img src="images/default_e.jpg"></td></tr>        
        </table>
    </center>
</body>
</html>