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
				<th colspan="3" align="center">关键词候选列表</th>
			</tr>
			<s:iterator value="stringRel" id="column">
				<table border="1">
					<tr>
						<td><s:property value="key" /></td>
						<td><input type="hidden" name="key"
							value=<s:property value="key" />></td>
						<td><s:iterator value="value" id="rel">
								<td width="200"><input type="checkbox" name="choose"
									value=<s:property value="rel" />> <s:property
										value="rel" /></td>
							</s:iterator></td>
					</tr>
				</table>
			</s:iterator>
			<tr>
				<td><input type="submit" name="submit" value="提交"></td>
			</tr>
		</table>
	</form>
	<a href="homepage.jsp">返回主页</a>
</body>
</html>