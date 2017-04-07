<%@page import="com.expense.utils.Props"%>
<html>
<head>
<title><%= Props.appName %></title>
<link rel="icon" type="image/png"
	href="<%= Props.appPath %>/images/favicon.png">
<link type="text/css" rel="stylesheet/less"
	href="<%= Props.appPath %>/css/expense.less">
<link type="text/css" rel="stylesheet/less"
	href="<%= Props.appPath %>/css/dojo.less">
<link rel="stylesheet"
	href="<%= Props.dojoPath %>/dijit/themes/claro/claro.css"
	media="screen">
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/less-1.3.0.min.js"></script>
<script type="text/javascript" src="<%= Props.dojoPath %>/dojo/dojo.js"
	data-dojo-config="isDebug: true,parseOnLoad: true"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/dojo_loader.js"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/messages.js"></script>
<script type="text/javascript" src="<%= Props.appPath %>/js/expense.js"></script>
<script type="text/javascript">
			loadDojo('validate','form');
			dojo.require("dojox.validate.check");
			
		    function init() {
		    	resizeDojoInputs( [ ['login','14em'], ['password','14em'] ] );

		    	dojo.connect(dijit.byId('loginbutton'), "onClick", submitForm);
		    	dojo.connect(dijit.byId('loginform'), "onSubmit", checkLogin);
		    	
		    	if(dojo.byId('alogin').value == 'Y' && dojo.byId('err').value != 'Y') {
		    		alogin();
		    	}
			}
		    
		    dojo.addOnLoad(init);
        </script>
</head>
<body class="claro">
	<div align="center" class="bodybg" style="height: 100%;">
		<div align="center" class="bodybg" style="height: 100%;">
			<div align="center">
				<div
					style="position: relative; top: 0px; width: 1024px; height: 100%"
					align="center">
					<form dojoType="dijit.form.Form" id="loginform" method="post"
						action="<%= Props.appPath %>/servlet/access/in">
						<div class="bodyDecision" align="left"
							style="position: relative; top: 250px; width: 394px; height: 194px;">
							<div class="sectionDecision"
								style="position: relative; top: 8px; left: 8px; width: 370px; height: 170px;">
								<table class="tableDecision" width="100%" cellspacing='0'
									cellpadding='3'>
									<tr style="height: 25px">
										<td colspan="2" align="center">
											<table width="90%" align="center">
												<tr style="height: 25px">
													<td align="left" class="loginHeader">Login</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr style="height: 40px" valign="middle">
										<td style="padding-left: 40px" valign="middle"><label
											for="login">Login</label></td>
										<td valign="middle"><input id="login" name="login"
											dojoType="dijit.form.ValidationTextBox" class="dashboardTxt"
											maxlength="10" required="true" /></td>
									</tr>
									<tr style="height: 40px" valign="top">
										<td style="padding-left: 40px" valign="top"><label
											for="password">Password</label></td>
										<td valign="top"><input id="password" name="password"
											type="password" dojoType="dijit.form.ValidationTextBox"
											class="dashboardTxt" maxlength="10" required="true" /></td>
									</tr>
									<tr style="height: 40px" valign="top">
										<td colspan="2" align="center"><button
												data-dojo-type="dijit.form.Button" id="loginbutton"
												class="dashboardTxt" type="submit">Login</button></td>
									</tr>
								</table>
								<div id="msg"
									style="position: relative; top: 25px; text-align: center;"
									class="msgError">${message}</div>
							</div>
						</div>
						<input type="hidden" id="alogin" name="alogin"
							value="<%= Props.aLogin %>"> <input type="hidden"
							id="err" name="err" value="${error}">
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>