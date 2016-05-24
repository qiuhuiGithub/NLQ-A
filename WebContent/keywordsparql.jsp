<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	Map<String, List<String>> map = new HashMap<String, List<String>>();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Translate Result</title>
</head>
<body>
	关键词：
	<s:property value="sparqlString" />
	<form action="general.action" method="post">
		<table align="center" border="1" style="width: 100%;">
			<tr>
				<th colspan="3" align="center">所选关系</th>
			</tr>
			<s:iterator value="stringRel" id="column">
				<table border="１">
					<tr>
						<td><s:property value="key" /></td>
						<td width="200"><s:property value="value" /></td>
					</tr>
				</table>
			</s:iterator>
		</table>
	</form>
	<table align="center" border="1" style="width: 100%;">
		<tr>
			<th colspan="3" align="center">SPARQL语句列表</th>
		</tr>
		<s:iterator value="sorted_map" id="column">
			<tr>
				<td><s:property value="key" />&nbsp;&nbsp;&nbsp;<s:property
						value="value" />&nbsp;&nbsp;&nbsp;<a
					href="sparql.action?queryType=1&sparqlString=${key}">查询</a></td>
			</tr>
		</s:iterator>
	</table>
	<a href="homepage.jsp">返回主页</a>
</body>
</html>