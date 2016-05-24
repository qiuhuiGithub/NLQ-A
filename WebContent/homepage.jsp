<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>homepage.jsp</title>
<style type="text/css">
<!--
a:link {
	text-decoration: none;
}

a:visited {
	text-decoration: underline;
	color: black;
}

a:hover {
	text-decoration: underline;
}

a:active {
	text-decoration: underline;
}

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.STYLE1 {
	font-size: 12px;
	color: black;
}

.STYLE5 {
	font-size: 12
}

.STYLE6 {
	font-size: 40
}

.STYLE7 {
	font-size: 12px;
	color: black;
}

-->
#txt {
	height: 246px;
	width: 512px;
	border: 1px solid #000000;
	position: relative
}

#txt p {
	position: absolute;
	bottom: 0px;
	padding: 0px;
	margin: 0px
}
</style>
</head>

<body bgcolor="white">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="57" background="images/main_03.gif"><table
					width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="378" height="57" background="images/main_01.gif">&nbsp;</td>
						<td>&nbsp;</td>
						<td width="281" valign="bottom"><table width="100%"
								border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="33" height="27"><img src="images/main_05.gif"
										width="33" height="27" /></td>
									<td width="248" background="images/main_06.gif"><table
											width="225" border="0" align="center" cellpadding="0"
											cellspacing="0">
											<!--    <tr>
                <td height="17"><div align="right"><img src="images/pass.gif" width="69" height="17" /></div></td>
                <td><div align="right"><img src="images/user.gif" width="69" height="17" /></div></td>
                <td><div align="right"><img src="images/quit.gif" width="69" height="17" /></div></td>
              </tr> -->

											<!--   mark -->
											<!--   <td hight="17"><div align="right"><a href="regist.jsp"><font size=2>注册</font></a></td>
              <td hight="17"><div align="right"><a href="login.jsp"><font size=2>登录</font></a></td> -->
										</table></td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
		<tr>
			<td height="40" background="images/main_10.gif"><table
					width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="194" height="40" background="images/main_07.gif">&nbsp;</td>
						<td><table width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td width="21"><img src="images/main_13.gif" width="19"
										height="14" /></td>
									<td width="35" class="STYLE7"><div align="center">首页</div></td>
									<td width="21" class="STYLE7"><img
										src="images/main_15.gif" width="19" height="14" /></td>
									<td width="35" class="STYLE7"><div align="center">后退</div></td>
									<td width="21" class="STYLE7"><img
										src="images/main_17.gif" width="19" height="14" /></td>
									<td width="35" class="STYLE7"><div align="center">前进</div></td>
									<td width="21" class="STYLE7"><img
										src="images/main_19.gif" width="19" height="14" /></td>
									<td width="35" class="STYLE7"><div align="center">刷新</div></td>
									<td width="21" class="STYLE7"><img
										src="images/main_21.gif" width="19" height="14" /></td>
									<td width="35" class="STYLE7"><div align="center">帮助</div></td>
									<td>&nbsp;</td>
								</tr>
							</table></td>
						<td width="248" background="images/main_11.gif"><table
								width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="16%"><span class="STYLE5"></span></td>
									<td width="75%"><div align="center">
											<span class="STYLE7">开发商：哈尔滨工业大学</span>
										</div></td>
									<td width="9%">&nbsp;</td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
	</table>
	<br>
	<br>
	<br>
	<br>
	<form action="sparql.action" method="post">
		<table height="80px" align="center">
			<tr height="80px">
				<td rowspan="2"><img src="images/logo.png" /></img></td>
				<!-- <td width="120"><a href="http://www.baidu.com">
    			<font size=5 color=black face="楷体">设备管理</font>
    			</a>
    		</td> -->
				<td width="200"><input type="radio" name="queryType" value="1"
					checked>SPARQL查询</td>
				<td width="200"><input type="radio" name="queryType" value="2">自然语言查询</td>
				<td width="200"><input type="radio" name="queryType" value="3">关键词查询</td>
			</tr>
		</table>
		<br>
		<table align="center">
			<tr>
				<td><input type="text" name="sparqlString" maxlength="1000"
					style="width: 700px; height: 40px;"></td>
				<td border="1"><input type="submit" name="submit"
					value="gStore一下" style="height: 40px;""></td>
			</tr>
		</table>
	</form>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<table width="1002" cellpadding="0" cellspacing="0" border="0"
		align="center" bgcolor="#FFFFFF">
		<tr>
			<td height="1" bgcolor="#cdcdcd"></td>
		</tr>
		<tr>
			<td><table width="1002" align="center" height="71">
					<tr>
						<!--   <td  width="300" align="center"><img src="images/teamwe.jpg" width="286" height="65" /></td> -->
						<td width="200">&nbsp;</td>
						<!-- <td><a href="login.jsp"><img src="images/admin.jpg" /></td>
            <td><a href="login.jsp"><img src="images/date.jpg" /></a></td>
              <td><a href="login.jsp"><img src="images/user.jpg" /></a></td> -->
						<td width="600" align="center">copyright(C) 哈尔滨工业大学<br>联系我们:qiuhui@hit.edu.cn
						</td>
						<td><select
							style="FONT-SIZE: 9pt; color: #000000; WIDTH: 180px; margin: 4px 0 0 2px;"
							onChange="window.open(this.options[this.selectedIndex].value,'_Blank')"
							size=1 name=select1>
								<option value="#" selected>-- 友情链接 --</option>

								<option value="http://www.hit.edu.cn" target=_blank
									style="color: #535353">
									百度一下，你就知道</a>
								</option>

								<option value="http://news.hit.edu.cn" target=_blank
									style="color: #535353">
									哈工大新闻网</a>
								</option>

								<option value="http://today.hit.edu.cn" target=_blank
									style="color: #535353">
									今日哈工大</a>
								</option>

								<option value="http://www.miit.gov.cn/" target=_blank
									style="color: #535353">
									搜狐视频</a>
								</option>

								<option value="http://lamd.hrbeu.edu.cn/" target=_blank
									style="color: #535353">
									网易邮箱</a>
								</option>

								<option value="http://asset.buaa.edu.cn/" target=_blank
									style="color: #535353">
									腾讯首页</a>
								</option>

								<option value="http://www.nwpu.edu.cn/guozichu/" target=_blank
									style="color: #535353">
									腾讯游戏</a>
								</option>

								<option value="http://gzc.nuaa.edu.cn/" target=_blank
									style="color: #535353">
									腾讯QQ</a>
								</option>

								<option value="http://www.bit.edu.cn/xxgk/gljg/xzjg/"
									target=_blank style="color: #535353">
									腾讯空间</a>
								</option>

								<option value="http://www.njust.edu.cn/" target=_blank
									style="color: #535353">
									腾讯娱乐</a>
								</option>

						</select></td>
					</tr>
				</table></td>
		</tr>
	</table>
</body>
</html>

