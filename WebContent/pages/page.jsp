<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s2"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分页导航栏</title>
</head>
<body>
	 <table border="0" width="100%" cellspacing="0">
        <tr>
            <td width="60%"><s2:property escape="false" value="#request.createPage.PageInfo"/></td>
            <td align="center" width="40%"><s2:property escape="false" value="#request.createPage.PageLink"/></td>
        </tr>
    </table>
</body>
</html>			