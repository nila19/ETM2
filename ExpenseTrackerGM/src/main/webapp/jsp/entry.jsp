<%@page import="com.expense.utils.Props"%>
<%@page
	import="java.util.List, java.util.Map, java.util.Date, org.apache.commons.lang3.time.DateUtils"%>
<%@page
	import="com.expense.utils.Utils, com.expense.mvc.model.ui.CategoryUI, com.expense.mvc.model.ui.AccountUI, com.expense.mvc.model.ui.DataKeyUI"%>
<%@page
	import="com.expense.mvc.model.ui.DescriptionUI, com.expense.mvc.model.ui.TransactionUI, com.expense.mvc.model.entity.Transaction"%>
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
			loadDojo('validate','form','grid','dialog','upload');
			dojo.require("dojox.validate.check");
			dojo.require("dojo.date");
			dojo.require("dojo.on");
			dojo.require("dojo.dom-construct");
			dojo.require("dojo.dom");
			dojo.require("dojo.domReady!");
			
		    function init() {
		    	resizeDojoInputs( [ ['description','21em'], ['amount','7em'], ['transDate','8em'] ] );
				dojo.connect(dijit.byId('adjustInd'), "onChange", toggleAdjAdd);
		    	dojo.connect(dijit.byId('m_adjustInd'), "onChange", toggleAdjMod);
		    	dojo.connect(dijit.byId('m_fromAccountId'), "onChange", loadFromBills);
		    	dojo.connect(dijit.byId('m_toAccountId'), "onChange", loadToBills);
		    	
				dijit.byId('dataKey').set('value', dojo.byId('dKey').value);
				
				reloadEntryData();
			}
		    
		    dojo.addOnLoad(init);
        </script>
</head>
<%
	List<CategoryUI> categories = (List<CategoryUI>)session.getAttribute("login_categories");
	List<AccountUI> accounts = (List<AccountUI>)session.getAttribute("login_accounts");
	List<AccountUI> accounts_all = (List<AccountUI>)session.getAttribute("login_accounts_all");
	List<DescriptionUI> descs = (List<DescriptionUI>)session.getAttribute("login_descriptions");
	DataKeyUI datakey = (DataKeyUI) session.getAttribute("login_datakey");

	request.setAttribute("CURR_PAGE","ENTRY");
%>
<body class="claro">
	<jsp:include page="header.jsp"></jsp:include>
	<div align="center" class="bodybg" style="height: 610px;">
		<div align="center">
			<div id="wholepage"
				style="position: relative; top: 0px; width: 100%; height: 100%;"
				align="left">
				<form id="entryForm" name="entryForm" method="post">

					<div class="bodyDecision" align="left"
						style="position: relative; top: 0px; left: 0px; width: 100%; height: 609px;">

						<div class="sectionDecision"
							style="position: relative; top: 2px; left: 2px; width: 1352px; height: 343px;">
							<div id="tallyPanel" class="sectionDecision"
								style="position: relative; top: 0px; left: 0px; width: 836px; height: 341px; overflow: auto;">
								<table class="tableDecision" width="100%" cellspacing='1'
									cellpadding='2'
									style="position: relative; top: -6px; left: -6px; width: 828px;">
									<tr style="height: 40px" valign="middle">
										<td width="19%" valign="middle" class="tallyGreenHL"
											align="center" onclick="listExpenses(0,false,0)">ACCOUNT</td>
										<td width="5%" valign="middle" class="tallyGreen"
											align="center">&nbsp;</td>
										<td width="12%" valign="middle" class="tallyGreen"
											align="center">A/C BAL $</td>
										<td width="12%" valign="middle" class="tallyGreen"
											align="center">TALLY $</td>
										<td width="18%" valign="middle" class="tallyGreen"
											align="center">TALLY DT</td>
										<td width="10%" valign="middle" class="tallyGreenHL"
											align="center" onclick="javascript:listExpenses(0,true,0)">$
											&Delta;</td>
										<td width="6%" valign="middle" class="tallyGreenHL"
											align="center" onclick="javascript:listExpenses(0,true,0)">#
											&Delta;</td>
										<td width="12%" valign="middle" class="tallyGreen"
											align="center">BILL BAL $</td>
										<td width="6%" valign="middle" align="center">&nbsp;</td>
									</tr>
									<%
			for(AccountUI acct : accounts) {
				int acctId = acct.getAccountId();
%>
									<tr style="height: 40px" valign="middle">
										<td align="center" class="tallyBlueHL"
											onclick="javascript:listExpenses(<%= acctId %>, false, 0)"><%= acct.getDescription() %></td>
										<td align="center" class="tallyBlueLight">
											<div
												style="position: relative; top: 0px; left: 7px; width: 100%; height: 20px;"
												id='ACTYPE_<%= acctId %>'></div>
										</td>
										<td align="right" class="tallyBlueLight"
											id='BALANCE_<%= acctId %>'></td>
										<td align="right" class="tallyBlueLight"
											id='TALLYBAL_<%= acctId %>'></td>
										<td align="center" id='TALLYDT_<%= acctId %>'></td>
										<td align="right"
											onclick="javascript:listExpenses(<%= acctId %>,true,0)"
											id='PENDBAL_<%= acctId %>'></td>
										<td align="right"
											onclick="javascript:listExpenses(<%= acctId %>,true,0)"
											id='PENDCNT_<%= acctId %>'></td>
										<td align="right" class="tallyBlueLight"
											onclick="javascript:listBills(<%= acctId %>)"
											id='BILLBAL_<%= acctId %>'></td>
										<td align="left">
											<div
												style="position: relative; top: 2px; left: 2px; width: 30px; height: 30px;"
												title="Tally" onclick="tallyAccount(<%= acctId %>)"
												class="tallyButton">
												<div
													style="position: relative; top: 3px; left: 3px; width: 24px; height: 24px;"
													class="tallyButtonInner"></div>
											</div>
										</td>
									</tr>
									<%
			}
%>
								</table>
							</div>

							<div id="entryPanel" class="sectionDecision"
								style="position: relative; top: -349px; left: 848px; width: 496px; height: 337px;">
								<table class="tableDecision" cellspacing='1' cellpadding='3'
									style="position: relative; top: -3px; left: -3px; width: 502px;">
									<tr style="height: 40px" valign="middle">
										<td colspan="4">
											<table class="tableDecision" width="100%" cellspacing='1'
												cellpadding='3'>
												<tr style="height: 20px" valign="middle">
													<td width="30%">&nbsp;</td>
													<td width="35%">
														<div
															style='position: relative; top: 0px; left: 0px; height: 20px;'>
															<input id="adjustInd" name="adjustInd"
																dojoType="dijit.form.CheckBox" value="Y" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ADJUST
															<div
																style='position: relative; top: -17px; left: 17px; width: 16px; height: 16px;'
																title='Adjustment' class='dgAdj'></div>
														</div>
													</td>
													<td width="35%">
														<div
															style='position: relative; top: 0px; left: 0px; height: 20px;'>
															<input id="adhocInd" name="adhocInd"
																dojoType="dijit.form.CheckBox" value="Y" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ADHOC
															<div
																style='position: relative; top: -15px; left: 17px; width: 16px; height: 16px;'
																title='Adhoc' class='dgAdhoc'></div>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr style="height: 40px" valign="middle">
										<td align="right">FROM :&nbsp;</td>
										<td colspan="3"><select id="fromAccountId"
											name="fromAccountId" style="width: 294px;"
											dojoType="dijit.form.FilteringSelect"
											data-dojo-props="queryExpr: '*\${0}*',autoComplete: false"
											class="dashboardTxt">
												<option value="0"></option>
												<%
				for (AccountUI acct : accounts) {
%>
												<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
												<%
				}
%>
										</select></td>
									</tr>
									<tr style="height: 40px" valign="middle">
										<td align="right">TO :&nbsp;</td>
										<td colspan="3"><select id="toAccountId"
											name="toAccountId" disabled="disabled" style="width: 294px;"
											dojoType="dijit.form.FilteringSelect"
											data-dojo-props="queryExpr: '*\${0}*',autoComplete: false"
											class="dashboardTxt">
												<option value="0"></option>
												<%
				for (AccountUI acct : accounts) {
%>
												<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
												<%
				}
%>
										</select></td>
									</tr>
									<tr style="height: 40px" valign="middle">
										<td align="right">CATG :&nbsp;</td>
										<td colspan="3"><select id="categoryId" name="categoryId"
											style="width: 294px;" dojoType="dijit.form.FilteringSelect"
											data-dojo-props="queryExpr: '*\${0}*',autoComplete: false"
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
										<td align="right">DESC :&nbsp;</td>
										<td colspan="3"><select id="description"
											name="description" data-dojo-type="dijit.form.ComboBox"
											data-dojo-props="queryExpr: '*\${0}*',autoComplete: false"
											maxlength="50" required="true" class="dashboardTxt">
												<option></option>
												<%
				for (DescriptionUI desc : descs) {
%>
												<option><%= desc.getDescription() %></option>
												<%
				}
%>
										</select></td>
									</tr>
									<tr style="height: 40px" valign="middle">
										<td width="22%" align="right">AMT :&nbsp;</td>
										<td width="25%"><input id="amount" name="amount"
											dojoType="dijit.form.CurrencyTextBox" class="dashboardTxt"
											maxlength="10" required="true" /></td>
										<td width="12%" align="right">DATE :&nbsp;</td>
										<td width="41%"><input id="transDate" name="strTransDate"
											dojoType="dijit.form.DateTextBox" class="dashboardTxt"
											required="true" /></td>
									</tr>
									<tr style="height: 50px" valign="middle">
										<td align="center" colspan="3"><div id="msg"
												style="position: relative; top: 8px; height: 45px; overflow: auto;"></div></td>
										<td align="center"><button
												data-dojo-type="dijit.form.Button" class="dashboardTxt"
												data-dojo-props="onClick:function(){ javascript:checkAdd(); }">ADD</button></td>
									</tr>
									<tr style="height: 42px" valign="middle">
										<td colspan="4">
											<table class="tableDecision" width="100%" cellspacing='1'
												cellpadding='3'>
												<tr style="height: 38px" valign="middle">
													<td width="33%" align="center" class="pf pfgreen"
														id="CREDITS"></td>
													<td width="33%" align="center" class="pf pfred" id="DEBITS"></td>
													<td width="34%" align="center" class="pf pfblue"
														id="BALANCES"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>

							<div id="billsPanel" class="sectionDecision"
								style="position: relative; top: -349px; left: 848px; width: 496px; height: 337px; overflow: auto; display: none;">
								<table class="tableDecision" width="100%" cellspacing='1'
									cellpadding='2'
									style="position: relative; top: -4px; left: -4px; width: 486px;">
									<tr style="height: 35px" valign="middle">
										<td width="10%" valign="middle" class="menuBarDKBlue"
											align="center">
											<div
												style="position: relative; top: 0px; left: 0px; width: 40px; height: 32px;">
												<div
													style="position: relative; top: 4px; left: 10px; width: 24px; height: 24px;"
													title="Enter Expense" onclick="entryPanel()"
													class="entryPanel"></div>
											</div>
										</td>
										<td width="90%" valign="middle" class="menuBarDKBlue"
											align="center">
											<div id="b_acctDec"
												style="position: relative; top: 8px; left: 0px; width: 100%; height: 30px;"></div>
										</td>
									</tr>
								</table>
								<table id="billsList" class="tableDecision" width="100%"
									cellspacing='1' cellpadding='2'
									style="position: relative; top: -4px; left: -4px; width: 486px;">
									<tr id="billHead" style="height: 35px" valign="middle">
										<td width="18%" valign="middle" class="tallyGreen"
											align="center">BILL DT</td>
										<td width="17%" valign="middle" class="tallyGreen"
											align="center">DUE DT</td>
										<td width="20%" valign="middle" class="tallyGreen"
											align="center">BILL $</td>
										<td width="20%" valign="middle" class="tallyGreen"
											align="center">BALANCE $</td>
										<td width="17%" valign="middle" class="tallyGreenHL"
											align="center">PAID DT</td>
										<td width="8%" valign="middle" class="tallyGreenHL"
											align="center">&nbsp;</td>
									</tr>
								</table>
							</div>

						</div>

						<div id="transPanel" class="sectionDecision"
							style="position: relative; top: 2px; left: 2px; width: 1352px; height: 257px;">
							<div id="transgriddiv"
								style="position: relative; width: 100%; height: 255px;"></div>
						</div>
					</div>
				</form>
				<form id="tallyForm" name="tallyForm" method="post">
					<input type="hidden" id="acctId" name="acctId" value="0"> <input
						type="hidden" id="pending" name="pending" value="false"> <input
						type="hidden" id="billId" name="billId" value="0"> <input
						type="hidden" id="tId" name="tId"> <input type="hidden"
						id="pageId" name="pageId" value="ENTRY"> <input
						type="hidden" id="dKey" name="dKey"
						value="<%= datakey.getDataKey() %>">
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
									name="fromAccountId" style="width: 160px;"
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
									class="dashboardTxt"></select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">To :&nbsp;</td>
								<td colspan="2"><select id="m_toAccountId"
									name="toAccountId" disabled="disabled" style="width: 160px;"
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
									style="width: 140px;" dojoType="dijit.form.Select"
									class="dashboardTxt"></select></td>
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

	<!-- Bill Pay Dialog -->
	<div class="dijitHidden">
		<div id="billPayDialog" data-dojo-type="dijit.Dialog"
			style="width: 350px;" align="center"
			data-dojo-props="title:'Pay Bill'">
			<form id="billPayForm" name="billPayForm">
				<div class="bodyDecision" align="left"
					style="position: relative; top: 0px; left: 0px; width: 100%; height: 260px;">
					<div class="sectionDecision"
						style="position: relative; top: 5px; left: 5px; width: 315px; height: 240px;">
						<table class="tableDecision" width="100%" cellspacing='1'
							cellpadding='3'>
							<tr style="height: 40px" valign="middle">
								<td align="right">Account :&nbsp;</td>
								<td><input id="bp_toAccount" name="toAccount"
									disabled="disabled" dojoType="dijit.form.ValidationTextBox"
									style="width: 215px;" class="dashboardTxt" /></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Bill :&nbsp;</td>
								<td><select id="bp_toBillId" name="toBillId"
									style="width: 220px;" dojoType="dijit.form.Select"
									class="dashboardTxt"></select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">From :&nbsp;</td>
								<td><select id="bp_fromAccountId" name="fromAccountId"
									style="width: 215px;" dojoType="dijit.form.FilteringSelect"
									data-dojo-props="queryExpr: '*\${0}*',autoComplete: false"
									class="dashboardTxt">
										<option value="0"></option>
										<%
				for (AccountUI acct : accounts_all) {
%>
										<option value='<%= acct.getAccountId() %>'><%= acct.getDescription() %></option>
										<%
				}
%>
								</select></td>
							</tr>
							<tr style="height: 40px" valign="middle">
								<td align="right">Date :&nbsp;</td>
								<td><input id="bp_transDate" name="strTransDate"
									dojoType="dijit.form.DateTextBox" style="width: 215px;"
									class="dashboardTxt" required="true" /></td>
							</tr>
						</table>
						<div style="width: 100%; position: relative; top: 5px;"
							align="center">
							<button dojoType="dijit.form.Button" class="dashboardTxt"
								data-dojo-props="onClick:function(){ javascript:checkBillPay(); }">Save</button>
							<button dojoType="dijit.form.Button" class="dashboardTxt"
								data-dojo-props="onClick:function(){ javascript:hideBillPayDialog(); }">Close</button>
						</div>
						<div id="msgBillPay"
							style="position: relative; top: 8px; height: 45px; overflow: auto;"></div>
					</div>
				</div>
			</form>
		</div>
	</div>

	<!-- Upload Trans Dialog -->
	<div class="dijitHidden">
		<div id="uploadDialog" data-dojo-type="dijit.Dialog"
			style="width: 500px;" align="center"
			data-dojo-props="title:'Upload Transaction'">
			<div class="bodyDecision" align="left"
				style="position: relative; top: 0px; left: 0px; width: 100%; height: 280px;">
				<div class="sectionDecision"
					style="position: relative; top: 5px; left: 5px; width: 465px; height: 260px;">
					<form id="uploadForm" name="uploadForm" method="post"
						enctype="multipart/form-data"
						action="<%= Props.appPath %>/servlet/upload/process">
						<table class="tableDecision" width="100%" cellspacing='1'
							cellpadding='3'>
							<tr style="height: 30px" valign="middle">
								<td width="23%" align="right">File Path :</td>
								<td width="2%">&nbsp;</td>
								<td width="35%"><input id="file" name="file" type="file"
									data-dojo-type="dojox.form.Uploader"
									data-dojo-props='label:"Select Some Files", onComplete:function(result){ javascript:processUpload(result); }' />
								</td>
								<td width="40%" align="center">
									<button id="btnUpload" type="submit"
										dojoType="dijit.form.Button" class="dashboardTxt">Upload</button>
								</td>
							</tr>
						</table>
						<div
							style="width: 100%; position: relative; top: 15px; height: 100px;">
							<div id="fileslist" data-dojo-type="dojox.form.uploader.FileList"
								data-dojo-props='uploaderId:"file"'></div>
						</div>
						<div id="msgUpload" style="position: relative; top: 38px;"></div>
					</form>
				</div>
			</div>
		</div>
	</div>

</body>
</html>

