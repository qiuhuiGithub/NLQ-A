<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
<body>
	<table align="center" border="1" style="width: 100%;">
		<tr>
			<th colspan="11" align="center">result</th>
		</tr>
		<tr>
			<td><s:property value="answerNumber" /></td>
		</tr>
		<tr>
			<td><s:property value="answer" /></td>
		</tr>
	</table>
	<a href="homepage.jsp">返回主页</a>
</body>
</html>