<%@page import="java.util.List, java.util.Map, java.util.Date"%>
<%@page
	import="com.expense.utils.Utils, com.expense.utils.FormatUtils, com.expense.utils.Props"%>
<%@page
	import="com.expense.mvc.model.ui.CategoryUI, com.expense.mvc.model.ui.MonthUI, com.expense.mvc.model.ui.AccountUI"%>
<%@page
	import="com.expense.mvc.model.entity.Transaction, com.expense.mvc.model.ui.TransactionUI, com.expense.mvc.model.ui.DataKeyUI"%>
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
<link rel="stylesheet"
	href="<%= Props.dojoPath %>/dojox/grid/resources/Grid.css"
	media="screen">
<link rel="stylesheet"
	href="<%= Props.dojoPath %>/dojox/grid/resources/claroGrid.css"
	media="screen">
<link rel="stylesheet"
	href="<%= Props.dojoPath %>/dojox/grid/enhanced/resources/claro/EnhancedGrid.css"
	media="screen">
<style type="text/css">
.dojoxGrid table {
	margin: 0;
}

html, body {
	width: 100%;
	height: 100%;
	margin: 0;
}
</style>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/less-1.3.0.min.js"></script>
<script type="text/javascript" src="<%= Props.dojoPath %>/dojo/dojo.js"
	data-dojo-config="isDebug: true,parseOnLoad: true"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/dojo_loader.js"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/messages.js"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/ajaxblocker.js"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/util/datagrid.js"></script>
<script type="text/javascript" src="<%= Props.appPath %>/js/expense.js"></script>
<script type="text/javascript"
	src="<%= Props.appPath %>/js/expense_ajax.js"></script>
<script type="text/javascript">
			loadDojo('validate','form','grid','dialog');
			dojo.require("dojox.validate.check");
			dojo.require("dojo.date");
			
		    function init() {
		    	resizeDojoInputs( [ ['description','15em'], ['amount','8em'], ['m_description','18em'], ['m_amount','18em'], ['m_transDate','18em'] ] );
		    	initSearchFields();
				excuteSearch();

		    	dojo.connect(dijit.byId('m_adjustInd'), "onChange", toggleAdjMod);
		    	dojo.connect(dijit.byId('m_fromAccountId'), "onChange", loadFromBills);
		    	dojo.connect(dijit.byId('m_toAccountId'), "onChange", loadToBills);
			}
		    
		    dojo.addOnLoad(init);
        </script>
</head>
<%
	List<CategoryUI> categories = (List<CategoryUI>)session.getAttribute("login_categories");
	List<AccountUI> accounts = (List<AccountUI>)session.getAttribute("login_accounts");
	List<AccountUI> accounts_all = (List<AccountUI>)session.getAttribute("login_accounts_all");
	
	List<Date> transMonths = (List<Date>) session.getAttribute("login_transmonths");
	List<Date> entryMonths = (List<Date>) session.getAttribute("login_entrymonths");

	TransactionUI tui = (TransactionUI) request.getAttribute("tui");
	
	request.setAttribute("CURR_PAGE","SEARCH");
%>
<body class="claro">
	<jsp:include page="header.jsp"></jsp:include>
	<div align="center" class="bodybg" style="height: 610px;">
		<div align="center">
			<div id="wholepage"
				style="position: relative; top: 0px; width: 100%; height: 100%;"
				align="left">
				<form id="searchForm" name="searchForm" method="post">

					<div class="bodyDecision" align="left"
						style="position: relative; top: 0px; left: 0px; width: 100%; height: 609px;">
						<div class="sectionDecision"
							style="position: relative; top: 2px; left: 2px; width: 1352px; height: 62px; overflow: auto;">
							<table class="tableDecision" width="100%" cellspacing='0'
								cellpadding='4'
								style="position: relative; top: -1px; left: -1px;">
								<tr height="20" valign="middle">
									<td width="18%" class="searchHeader" align="center">Category</td>
									<td width="15%" class="searchHeader" align="center">Description</td>
									<td width="12%" class="searchHeader" align="center">Amount</td>
									<td width="9%" class="searchHeader" align="center">Exp
										Month</td>
									<td width="9%" class="searchHeader" align="center">Entry
										Month</td>
									<td width="10%" class="searchHeader" align="center">Account</td>
									<td width="7%" class="searchHeader" align="center">Adjust</td>
									<td width="7%" class="searchHeader" align="center">Adhoc</td>
									<td width="13%" class="searchHeader" align="center" rowspan="2"
										valign="middle">
										<button data-dojo-type="dijit.form.Button"
											class="dashboardTxt"
											data-dojo-props="onClick:function(){ javascript:excuteSearch(); }">Search</button>
										&nbsp;
									</td>
								</tr>
								<tr height="20" valign="middle">
									<td class="searchHeaderGreyTxt" align="center"><select
										id="categoryId" name="categoryId" style="width: 220px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value="0"></option>
											<%
				for (CategoryUI cat : categories) {
%>
											<option value='<%= cat.getCategoryId() %>'><%= cat.getCategoryDesc() %></option>
											<%
				}
%>
									</select></td>
									<td class="searchHeaderGreyTxt" align="center"><input
										id="description" name="description"
										dojoType="dijit.form.ValidationTextBox" class="dashboardTxt"
										maxlength="50" /></td>
									<td class="searchHeaderGreyTxt" align="center"><input
										id="amount" name="amount"
										dojoType="dijit.form.CurrencyTextBox" class="dashboardTxt"
										maxlength="50" value="0" /></td>
									<td class="searchHeaderGreyTxt" align="center"><select
										id="strTransMonth" name="strTransMonth" style="width: 80px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value="0"></option>
											<%
				for (Date mon : transMonths) {
%>
											<option value='<%= FormatUtils.yyyyMM.format(mon) %>'><%= FormatUtils.MMMyy.format(mon) %></option>
											<%
				}
%>
									</select></td>
									<td class="searchHeaderGreyTxt" align="center"><select
										id="strEntryMonth" name="strEntryMonth" style="width: 80px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value="0"></option>
											<%
				for (Date mon : entryMonths) {
%>
											<option value='<%= FormatUtils.yyyyMM.format(mon) %>'><%= FormatUtils.MMMyy.format(mon) %></option>
											<%
				}
%>
									</select></td>
									<td class="searchHeaderGreyTxt" align="center"><select
										id="fromAccountId" name="fromAccountId" style="width: 160px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value="0"></option>
											<%
				for (AccountUI acct : accounts_all) {
%>
											<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
											<%
				}
%>
									</select></td>
									<td class="searchHeaderGreyTxt" align="center"><select
										id="adjustInd" name="adjustInd" style="width: 60px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value='0'>ALL</option>
											<option value='Y'>YES</option>
											<option value='N'>NO</option>
									</select></td>
									<td class="searchHeaderGreyTxt" align="center"><select
										id="adhocInd" name="adhocInd" style="width: 60px;"
										dojoType="dijit.form.Select" class="dashboardTxt">
											<option value='0'>ALL</option>
											<option value='Y'>YES</option>
											<option value='N'>NO</option>
									</select></td>
								</tr>
							</table>
						</div>
						<div class="sectionDecision"
							style="position: relative; top: 4px; left: 2px; width: 1352px; height: 536px;">
							<div id="searchgriddiv"
								style="position: relative; width: 100%; height: 500px;"></div>
							<div
								style="position: relative; top: 4px; left: 0px; width: 1300px; height: 30px;">
								<div id="searchtotaldiv"
									style="position: relative; top: 0px; left: 612px; width: 105; height: 25px; vertical-align: middle; padding-top: 7px;"
									class="searchTotal" align="center"></div>
								<div id="msg"
									style="position: relative; top: -35px; left: 880px; width: 420px; height: 25px; padding-top: 5px; overflow: auto;"></div>
							</div>
						</div>
					</div>
					<input type="hidden" id="summaryDrillDown" name="summaryDrillDown"
						value="<%= tui != null ? "Y" : "N" %>"> <input
						type="hidden" id="category" name="category"
						value="<%= tui != null ? tui.getCategoryId() : "0" %>"> <input
						type="hidden" id="adhoc" name="adhoc"
						value="<%= tui != null ? tui.getAdhocInd() : "0" %>"> <input
						type="hidden" id="adjust" name="adjust"
						value="<%= tui != null ? tui.getAdjustInd() : "0" %>"> <input
						type="hidden" id="transMon" name="transMon"
						value="<%= tui != null ? FormatUtils.yyyyMM.format(tui.getTransMonth()) : "0" %>">
					<input type="hidden" id="tId" name="tId"> <input
						type="hidden" id="pageId" name="pageId" value="SEARCH">
				</form>
			</div>
		</div>
	</div>

	<!-- Confirm Trans Delete -->
	<div id="deleteTransDialog" data-dojo-type="dijit.Dialog"
		style="position: relative; left: 0px; top: 0px; width: 500px; height: 160px;"
		data-dojo-props="title:'Confirmation',loadingMessage:'Loading dialog content...', preventCache:true">
		<div id="deleteTransMsg"
			style="position: relative; top: 0px; left: 0px; height: 75px; width: 480px; text-align: center; vertical-align: middle;">
			Are you sure you want to delete this transaction ?</div>
		<div style="width: 100%; position: relative; top: 5px;" align="center">
			<form id="deleteTransForm" name="deleteTransForm">
				<button dojoType="dijit.form.Button" onclick="deleteTrans();">Proceed</button>
			</form>
		</div>
	</div>

	<!-- Modify Trans Dialog -->
	<div class="dijitHidden">
		<div id="modifyTransDialog" data-dojo-type="dijit.Dialog"
			style="width: 500px;" align="center"
			data-dojo-props="title:'Modify Transaction'">
			<form id="modifyTransForm" name="modifyTransForm">
				<div class="bodyDecision" align="left"
					style="position: relative; top: 0px; left: 0px; width: 100%; height: 390px;">
					<div class="sectionDecision"
						style="position: relative; top: 5px; left: 5px; width: 465px; height: 370px;">
						<table class="tableDecision" width="100%" cellspacing='1'
							cellpadding='3'>
							<tr style="height: 30px" valign="middle">
								<td width="25%">&nbsp;</td>
								<td width="37%"><input id="m_adjustInd" name="adjustInd"
									dojoType="dijit.form.CheckBox" value="Y" />&nbsp;&nbsp;Adjustment
									?</td>
								<td width="37%"><input id="m_adhocInd" name="adhocInd"
									dojoType="dijit.form.CheckBox" value="Y" />&nbsp;&nbsp;Adhoc ?</td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">From :&nbsp;</td>
								<td colspan="2"><select id="m_fromAccountId"
									name="fromAccountId" style="width: 260px;"
									dojoType="dijit.form.Select" class="dashboardTxt">
										<option value="0"></option>
										<%
				for (AccountUI acct : accounts_all) {
%>
										<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
										<%
				}
%>
								</select>&nbsp; <select id="m_fromBillId" name="fromBillId"
									style="width: 140px;" dojoType="dijit.form.Select"
									class="dashboardTxt">
								</select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">To :&nbsp;</td>
								<td colspan="2"><select id="m_toAccountId"
									name="toAccountId" disabled="disabled" style="width: 260px;"
									dojoType="dijit.form.Select" class="dashboardTxt">
										<option value="0"></option>
										<%
				for (AccountUI acct : accounts_all) {
%>
										<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
										<%
				}
%>
								</select>&nbsp; <select id="m_toBillId" name="toBillId"
									disabled="disabled" style="width: 140px;"
									dojoType="dijit.form.Select" class="dashboardTxt">
								</select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Category :&nbsp;</td>
								<td colspan="2"><select id="m_categoryId" name="categoryId"
									style="width: 260px;" dojoType="dijit.form.Select"
									class="dashboardTxt">
										<option value="0"></option>
										<%
				for (CategoryUI cat : categories) {
%>
										<option value='<%= cat.getCategoryId() %>'><%= cat.getCategoryDesc() %></option>
										<%
				}
%>
								</select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Description :&nbsp;</td>
								<td colspan="2"><input id="m_description"
									name="description" dojoType="dijit.form.ValidationTextBox"
									class="dashboardTxt" maxlength="50" required="true" /></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Amount :&nbsp;</td>
								<td colspan="2"><input id="m_amount" name="amount"
									dojoType="dijit.form.CurrencyTextBox" class="dashboardTxt"
									maxlength="15" required="true" /></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Date :&nbsp;</td>
								<td colspan="2"><input id="m_transDate" name="strTransDate"
									dojoType="dijit.form.DateTextBox" class="dashboardTxt"
									required="true" /></td>
							</tr>
						</table>
						<div style="width: 100%; position: relative; top: 5px;"
							align="center">
							<button dojoType="dijit.form.Button" class="dashboardTxt"
								data-dojo-props="onClick:function(){ javascript:checkModifyTrans(); }">Save</button>
							<button dojoType="dijit.form.Button" class="dashboardTxt"
								data-dojo-props="onClick:function(){ javascript:hideModifyTransDialog(); }">Close</button>
						</div>
						<div id="msgMod"
							style="position: relative; top: 8px; height: 45px; overflow: auto;"></div>
					</div>
				</div>
				<input type="hidden" id="mod_transId" name="transId"> <input
					type="hidden" id="m_fromBillIdTran" name="m_fromBillIdTran">
				<input type="hidden" id="m_toBillIdTran" name="m_toBillIdTran">
			</form>
		</div>
	</div>

</body>
</html>

