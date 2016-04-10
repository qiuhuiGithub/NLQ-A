<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'querybyname.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>
  <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
<body bgcolor=ADD2DA>
	<table align="center" border="1" style="width:100%;">
		<tr>
			<th colspan="6" align="center">管理员列表</th>
		
		</tr>
		<tr>
			<th>用户名</th>
			<th>性别</th>
			<th>国籍</th>
			<th>邮箱</th>
			<th>改为数据员</th>
			<th>改为普通用户</th>
		 </tr>
		 <s:iterator value="adminlist">
			<tr>
				<td><s:property value="username" /></td>
				<td><s:property value="sex" /></td>
				<td><s:property value="country" /></td>
			    <td><s:property value="email" /></td>
			    <td><a href="change.action?id=1 & username=${username}">是</a></td>
			    <td><a href="change.action?id=2 & username=${username}">是</a></td>
			</tr>
	   </s:iterator>
	</table>
	 <a href="right.jsp">返回主页</a>
	</body>
	</html>