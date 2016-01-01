<%@page import="java.util.List, java.util.Map"%>
<%@page
	import="com.expense.utils.Utils, com.expense.utils.Props, com.expense.mvc.helper.SummaryHelper"%>
<%@page
	import="com.expense.mvc.model.ui.CategoryUI, com.expense.mvc.model.ui.MonthUI, com.expense.mvc.model.ui.DataKeyUI"%>
<%@page
	import="com.expense.mvc.model.ui.SummaryUI, com.expense.mvc.model.entity.Transaction"%>
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
<script type="text/javascript"
	src="<%= Props.appPath %>/js/expense_ajax.js"></script>
<script type="text/javascript">
			loadDojo('validate','form');
			
			var CURR_MONTHS;
			
		    function init() {
				dojo.connect(dijit.byId('foreCast'), "onChange", loadSummary);
				dojo.connect(dijit.byId('adhoc'), "onChange", loadSummary);
				dojo.connect(dijit.byId('regular'), "onChange", loadSummary);
				
				loadSummary();
			}
		    
		    dojo.addOnLoad(init);
        </script>
</head>
<%
	SummaryHelper helper = (SummaryHelper) session.getAttribute("summary_helper");
	request.setAttribute("CURR_PAGE","SUMMARY");
	
	int PAGE_SIZE = Integer.valueOf(Props.expense.getString("PAGE.SIZE"));
%>
<body class="claro" marginheight="0" marginwidth="0">
	<jsp:include page="header.jsp"></jsp:include>
	<div align="center" class="bodybg" style="height: 610px;">
		<div align="center">
			<div id="wholepage"
				style="position: relative; top: 0px; width: 100%; height: 100%;"
				align="left">
				<form id="summaryForm" name="summaryForm" method="post">

					<div class="bodyDecision" align="left"
						style="position: relative; top: 0px; left: 0px; width: 100%; height: 609px;">
						<div class="sectionDecision"
							style="position: relative; top: 2px; left: 2px; width: 1352px; height: 610px; overflow: auto;">
							<table class="tableDecision" cellspacing='1' cellpadding='8'
								style="position: relative; top: -2px; left: -2px;">
								<tr height="30" valign="middle">
									<td width="110px" class="sum-headerGreen" align="left"
										nowrap="nowrap"><input id="foreCast" name="foreCast"
										dojoType="dijit.form.CheckBox" value="true">&nbsp;&nbsp;Forecast
									</td>
									<td width="110px" class="sum-headerGrey" align="left"
										nowrap="nowrap">
										<div
											style="position: relative; top: 0px; left: 20px; width: 70px; height: 20px;">
											<div
												style="position: relative; top: 0px; left: 10px; width: 20px; height: 20px;"
												title="Previous" onclick="gotoSummaryPage(-1)"
												class="navLeft"></div>
											<div
												style="position: relative; top: -20px; left: 50px; width: 20px; height: 20px;"
												title="Next" onclick="gotoSummaryPage(1)" class="navRight"></div>
										</div>
									</td>
									<%
			for(int i = 0; i < PAGE_SIZE; i++) {
%>
									<td width="75px" id="month_<%= i %>" align="center"></td>
									<%
			}
%>
								</tr>
								<tr height="30" valign="middle">
									<td class="sum-headerGreen" align="left" nowrap="nowrap">
										<input id="adhoc" name="adhoc" dojoType="dijit.form.CheckBox"
										checked="checked" value="true">&nbsp;&nbsp;Adhoc
									</td>
									<td class="sum-headerGreen" align="left" nowrap="nowrap">
										<input id="regular" name="regular"
										dojoType="dijit.form.CheckBox" checked="checked" value="true">&nbsp;&nbsp;Regular
									</td>
									<%
			for(int i = 0; i < PAGE_SIZE; i++) {
%>
									<td id="monthtotal_<%= i %>" align="center"
										onclick="javascript:drillDown(<%= i %>, 0)"></td>
									<%
			}
%>
								</tr>
								<%
			for(CategoryUI cat : helper.getCategories()) {
				String[] colorSet = helper.getColorSet(cat);
%>
								<tr height="28" valign="middle">
									<td class="<%= colorSet[0] %>" align="center"><%= cat.getMainCategory() %></td>
									<td width="120px" class="<%= colorSet[0] %>" align="left"><%= cat.getSubCategory() %></td>
									<%
				for(int j = 0; j < PAGE_SIZE; j++) {
%>
									<td id="data_<%= cat.getCategoryId() %>_<%= j %>"
										align="center"
										onclick="javascript:drillDown(<%= j %>, <%= cat.getCategoryId() %>)"></td>
									<%
				}
			}
%>
								
							</table>
						</div>
					</div>
					<input type="hidden" id="foreCastInd" name="foreCastInd"
						value="false"> <input type="hidden" id="adhocInd"
						name="adhocInd" value="0"> <input type="hidden"
						id="categoryId" name="categoryId"> <input type="hidden"
						id="strTransMonth" name="strTransMonth"> <input
						type="hidden" id="adjustInd" name="adjustInd" value="N"> <input
						type="hidden" id="pageNum" name="pageNum" value="0">
				</form>
			</div>
		</div>
	</div>
</body>
</html>

