<%@taglib uri="/struts-tags" prefix="s"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String choosedOntology = (String) request
			.getAttribute("choosedOntology");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Translate Result</title>
</head>
<body>
	SPARQL语句：
	<s:property value="sparqlString" />
	<table align="center" border="1" style="width: 100%;">
		<tr>
			<th colspan="3" align="center">关系列表</th>
		</tr>
		<tr>
			<th align="center">主语列表</th>
			<th align="center">谓语列表</th>
			<th align="center">宾语列表</th>
		</tr>
		<s:iterator value="tripleList">
			<tr>
				<td><s:property value="subject" /></td>
				<td><s:property value="relation" /></td>
				<td><s:property value="object" /></td>
			</tr>
		</s:iterator>
	</table>
	<br>
	<br>
	<table align="center" border="1" style="width: 100%;">
		<tr>
			<th colspan="3" align="center">支持集列表</th>
		</tr>
		<tr>
			<th align="center">主语支持集</th>
			<td><s:iterator value="subSupportList" id="subject">
					<s:property value="subject" />, </s:iterator></td>
		</tr>

		<tr>
			<th align="center">谓语支持集</th>
			<td><s:iterator value="relPrepList" id="relation">
					<s:property value="relation" />, </s:iterator></td>
		</tr>

		<tr>
			<th align="center">宾语支持集</th>
			<td><s:iterator value="objSupportList" id="object">
					<s:property value="object" />, </s:iterator></td>
		</tr>

	</table>
	<br>
	<br>
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