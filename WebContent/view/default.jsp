<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>首页</title>
</head>
<body>
    <center>
        <!-- 缴费专区 -->
        <s:set name="payforlist" value="#request.payforlist"/>        
        <table border="0" width="670" cellspacing="0" cellpadding="5">
            <tr height="35"><td style="text-indent:5" valign="bottom"><font color="#004790"><b>■推荐信息</b>『缴费专区』</font></td></tr>
            <tr bgcolor="#FAFCF5">
                <td style="border:1 solid">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                        <s:if test="#payforlist==null||#payforlist.size()==0">
                            <tr height="30"><td align="center" style="border:1 solid">★★★ 缴费后，您发布信息就可在这里显示！★★★</td></tr>
                        </s:if>
                        <s:else>                
                            <s:iterator status="payforStatus" value="payforlist">
                                 <s:if test="#payforStatus.odd"><tr height="23"></s:if>
                                 <td width="50%">『<b><s:property value="#session.typeMap[infoType]"/></b>』<a href="info_SingleShow.action?id=<s:property value='id'/>"><s:property value="getSubInfoTitle(20)"/></a></td>
                                 <s:if test="#payforStatus.even"></tr></s:if>
                            </s:iterator>
                        </s:else>                        
                    </table>
                </td>
            </tr>
        </table>
        <!-- 广告 -->
        <table border="0" width="670"cellspacing="0" cellpadding="0" style="margin-top:1">
            <tr><td align="center"><img src="images/pcard2.jpg"></td></tr>
        </table>        
        <!-- 免费专区 -->
        <s:set name="allsublist" value="#request.allsublist"/>
        <table border="0" width="670" cellspacing="2" cellpadding="0">
            <tr height="35"><td colspan="2" style="text-indent:5" valign="bottom"><font color="#004790"><b>■最新信息</b>『免费专区』</font></td></tr>            
            <s:if test="#allsublist==null||#allsublist.size()==0">
                <tr height="30"><td align="center" style="border:1 solid">★★★ 在这里显示免费发布的信息！★★★</td></tr>
            </s:if>
            <s:else>
                <s:iterator status="allStatus" value="allsublist">
                    <s:if test="#allStatus.odd"><tr></s:if>
                    <td align="center">
                        <table border="1" cellspacing="0" cellpadding="0" width="332" height="160" rules="none" bordercolor="lightgrey" bordercolorlight="lightgrey" bordercolordark="white">
                            <s:iterator status="oneStatus">
                                <s:if test="#oneStatus.index==0">
                                  <tr bgcolor="#00B48F" height="30">
                                      <td style="text-indent:10"><b><font color="white">▲<s:property value="#session.typeMap[infoType]"/></font></b></td>                                      
                                  </tr>
                                </s:if>
                                <tr bgcolor="#FAFCF5"><td style="text-indent:3">★ <a href="info_SingleShow.action?id=<s:property value='id'/>"><s:property value="getSubInfoTitle(20)"/></a></td></tr>
                                <s:if test="#oneStatus.last">
                                    <tr height="20" bgcolor="#FAFCF5"><td align="right"><a href="info_ListShow.action?infoType=<s:property value='infoType'/>">更多...</a>&nbsp;&nbsp;</td></tr>                                    
                                </s:if>
                            </s:iterator>
                        </table>
                    </td>
                    <s:if test="#allStatus.even"></tr></s:if>
                </s:iterator>
            </s:else>
        </table>
        <br>
    </center>
</body>
</html>