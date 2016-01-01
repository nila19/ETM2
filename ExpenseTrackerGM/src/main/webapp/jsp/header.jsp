<%@page
	import="java.util.HashMap, java.util.List, java.util.Date, org.apache.commons.lang3.StringUtils"%>
<%@page
	import="com.expense.utils.FormatUtils, com.expense.utils.Props, com.expense.utils.Menus, com.expense.mvc.model.ui.DataKeyUI, com.expense.mvc.model.entity.DataKey"%>
<%
	String CURR_PAGE = (String) request.getAttribute("CURR_PAGE");
	List<DataKeyUI> dkeys = (List<DataKeyUI>) session.getAttribute("login_datakeys");
	DataKeyUI dkey = (DataKeyUI) session.getAttribute("login_datakey");
	
	String menuCss = (dkey.getStatus() == DataKey.Status.ACTIVE.status) ? "menuBarDKBlue" : "menuBarDKRed";
	String currencyCss = StringUtils.equalsIgnoreCase(dkey.getCurrency(), DataKey.Currency.USD.currency) ? "menuDKUSD" : "menuDKINR";
%>
<div align="left" style="position: relative; left: 0px; top: 0px;">
	<div align="left"
		style="position: relative; top: 0px; left: 2px; width: 1362px; height: 42px;">
		<table width="100%" cellspacing='1' cellpadding='0'>
			<tr height="40" valign="top">
				<%
	for(Menus.Page pg : Menus.Page.values()) {
		if(StringUtils.equalsIgnoreCase(pg.type, CURR_PAGE)) {
%>
				<td width="10%" align="center" class="menuSel"><%= pg.name %></td>
				<%
		} else {
%>
				<td width="10%" align="center" class="menuHL"
					onclick="javascript:gotoPage('<%= pg.url %>')"><%= pg.name %></td>
				<%
		}
	}
%>
				<td width="29%">&nbsp;</td>
				<td width="38%" align="center" class="menuBar" valign="top">
					<table width="100%" cellspacing='0' cellpadding='0'>
						<tr height="40" valign="top">
							<td width="26%" align="right" class="menuBar">
								<%
				if(StringUtils.equalsIgnoreCase(Menus.Page.ENTRY.type, CURR_PAGE)) {
%> <select id="dataKey" name="dataKey" style="width: 120px;"
								dojoType="dijit.form.Select" class="dashboardTxt">
									<%
						for (DataKeyUI dk : dkeys) {
%>
									<option value='<%= dk.getDataKey() %>'><%= dk.getDataKey() %></option>
									<%
						}
%>
							</select> <%
				}
%>
							</td>
							<td width="8%" align="left" class="menuBar">
								<%
				if(StringUtils.equalsIgnoreCase(Menus.Page.ENTRY.type, CURR_PAGE)) {
%>
								<div
									style="position: relative; top: 0px; left: 0px; width: 20px; height: 20px;"
									title="Go" onclick="changeDataKey()" class="menuDKGo">
									<%
				}
%>
									&nbsp;
							</td>
							<td width="1%" align="left" class="menuBar"></td>
							<td width="63%" align="center" valign="middle">
								<table width="100%" cellspacing='0' cellpadding='2' border="0">
									<tr height="30" valign="middle">
										<td width="30%" align="center" class="<%= menuCss %>"><%= dkey.getDescription() %>
										</td>
										<td width="50%" align="center" class="<%= menuCss %>"><%= dkey.getStrStartDt() %>
											- <%= dkey.getStrEndDt() %></td>
										<td width="20%" align="center" class="<%= menuCss %>">
											<div
												style="position: relative; top: 0px; left: 8px; width: 28px; height: 20px;"
												class="<%= currencyCss %>">
										</td>
									</tr>
								</table>
							</td>
							<td width="2%" align="left" class="menuBar"></td>
						</tr>
					</table>
				</td>
				<td width="3%" align="center" class="menuBar" valign="middle">
					<%
		if(StringUtils.equalsIgnoreCase(Menus.Page.ENTRY.type, CURR_PAGE)) {
%>
					<div
						style="position: relative; top: 0px; left: 10px; width: 20px; height: 20px;"
						title="Go" onclick="showUploadDialog()" class="menuUpload">
						<%
		}
%>
					
				</td>
			</tr>
		</table>
	</div>
</div>
