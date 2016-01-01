//******************************************************************************************
//*************************************** Login Page ***************************************

function submitForm() {
	var form = dojo.query("form")[0];
	if(checkLogin()) {
		form.submit();
	}
}

function checkLogin() {
	var form = dojo.query("form")[0];
	var loginprofile = {
		trim : [ "login", "password" ],
		required : [ "login", "password" ]
	};

	var results = dojox.validate.check(form, loginprofile);

	if (results.isSuccessful()) {
		return true;
	} else {
		dojo.byId("msg").innerHTML = ERROR_INCOMPLETE;
		return false;
	}
}

function alogin() {
	var form = dojo.query("form")[0];
	dijit.byId('login').set('value', 'xxxxx');
	dijit.byId('password').set('value', 'xxxxx');
	form.submit();
}

// ******************************************************************************************
// *************************************** Entry Page ***************************************

function toggleAdj(mod) {
	if (dijit.byId(mod + 'adjustInd').get('checked')) {
		dijit.byId(mod + 'adhocInd').set('checked', false);
		dijit.byId(mod + 'adhocInd').set('disabled', true);
		dijit.byId(mod + 'toAccountId').set('disabled', false);
		dijit.byId(mod + 'categoryId').set('value', 0);
		dijit.byId(mod + 'categoryId').set('disabled', true);
		if(dijit.byId(mod + 'toBillId')) {
			dijit.byId(mod + 'toBillId').set('disabled', false);
		}
	} else {
		dijit.byId(mod + 'adhocInd').set('disabled', false);
		dijit.byId(mod + 'toAccountId').set('value', 0);
		dijit.byId(mod + 'toAccountId').set('disabled', true);
		dijit.byId(mod + 'categoryId').set('disabled', false);
		if(dijit.byId(mod + 'toBillId')) {
			dijit.byId(mod + 'toBillId').set('value', 0);
			dijit.byId(mod + 'toBillId').set('disabled', true);
		}
	}
}

function populateDesc() {
	var desc = dijit.byId('description').get('value');
	if(desc == '') {
		var cat = dijit.byId('categoryId').get('displayedValue');
		dijit.byId('description').set('value', cat.split("~")[1]);
	}
	return false;
}

function checkAdd() {
	var form = dojo.byId('entryForm');
	var expProfile = {
		trim : [ "description" ],
		required : [ "fromAccountId", "categoryId", "description", "amount", "transDate" ]
	};
	var adjProfile = {
		trim : [ "description" ],
		required : [ "fromAccountId", "toAccountId", "description", "amount", "transDate" ]
	};

	var profile = dijit.byId('adjustInd').get('checked') ? adjProfile : expProfile;

	var results = dojox.validate.check(form, profile);

	if (results.isSuccessful()) {
		addExpense();
	} else {
		showMessage('msg', '1000', ERROR_INCOMPLETE);
	}
}

function clearAddFields(desc) {
	dijit.byId('description').set('value', '');
	dijit.byId('amount').set('value', 0);
	dijit.byId('description').get('store').add({ name: desc});
}

function reloadEntryData() {
	listExpenses(0, false, 0);
	reloadPFBal();
	reloadAcctBal();
}

function reloadEntryDataRetainFilter() {
	reloadTrans();
	reloadPFBal();
	reloadAcctBal();
}

function reloadEntryDataRetainBills() {
	reloadTrans();
	reloadAcctBal();
	reloadBills();
}

function listExpenses(acctId, pending, billId) {
	dojo.byId('acctId').value = acctId;
	dojo.byId('pending').value = pending;
	dojo.byId('billId').value = billId;

	reloadTrans();
}

function refreshPFBal(data) {
	dojo.byId("CREDITS").innerHTML = data['CREDITS'];
	dojo.byId("DEBITS").innerHTML = data['DEBITS'];
	dojo.byId("BALANCES").innerHTML = data['BALANCES'];
}

function entryPanel() {
	dojo.byId("billsPanel").style.display = 'none';
	dojo.byId("entryPanel").style.display = 'block';
}

function listBills(acctId) {
	dojo.byId("entryPanel").style.display = 'none';
	dojo.byId("billsPanel").style.display = 'block';
	dojo.byId('acctId').value = acctId;

	reloadBills();
	listExpenses(acctId, false, 0);
}

function refreshAcctBal(data) {
	var today = new Date();
	for (var i = 0; i < data.length; i++) {
		var acct = data[i];
		dojo.addClass('ACTYPE_' + acct.accountId, acct.cash?'acCash':'acCard');
		dojo.byId('BALANCE_' + acct.accountId).innerHTML = acct.fbalanceAmt;
		dojo.byId('TALLYBAL_' + acct.accountId).innerHTML = acct.ftallyBalance;
		dojo.byId('TALLYDT_' + acct.accountId).innerHTML = acct.ftallyDate;
		dojo.byId('PENDBAL_' + acct.accountId).innerHTML = acct.ftallyExpenseAmt;
		dojo.byId('PENDCNT_' + acct.accountId).innerHTML = acct.tallyExpenseCnt;
		dojo.byId('BILLBAL_' + acct.accountId).innerHTML = acct.fbillBalance;

		var tdate = new Date(acct.tallyDate2);
		if (dojo.date.compare(today, tdate, "date") != 0) {
			dojo.addClass('TALLYDT_' + acct.accountId, 'tallyRedLight');
			dojo.removeClass('TALLYDT_' + acct.accountId, 'tallyBlueLight');
		} else {
			dojo.addClass('TALLYDT_' + acct.accountId, 'tallyBlueLight');
			dojo.removeClass('TALLYDT_' + acct.accountId, 'tallyRedLight');
		}

		if (acct.tallyExpenseAmt != 0) {
			dojo.addClass('PENDBAL_' + acct.accountId, 'tallyBlueHL');
			dojo.removeClass('PENDBAL_' + acct.accountId, 'tallyBlueLight');
		} else {
			dojo.addClass('PENDBAL_' + acct.accountId, 'tallyBlueLight');
			dojo.removeClass('PENDBAL_' + acct.accountId, 'tallyBlueHL');
		}

		if (acct.tallyExpenseCnt != 0) {
			dojo.addClass('PENDCNT_' + acct.accountId, 'tallyBlueHL');
			dojo.removeClass('PENDCNT_' + acct.accountId, 'tallyBlueLight');
		} else {
			dojo.addClass('PENDCNT_' + acct.accountId, 'tallyBlueLight');
			dojo.removeClass('PENDCNT_' + acct.accountId, 'tallyBlueHL');
		}

		if (acct.billed) {
			if(acct.dueDtWarning) {
				dojo.addClass('BILLBAL_' + acct.accountId, 'tallyRedHL');
				dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyBlueHL');
				dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyBlueLight');
			} else {
				dojo.addClass('BILLBAL_' + acct.accountId, 'tallyBlueHL');
				dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyBlueLight');
				dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyRedHL');
			}
		} else {
			dojo.addClass('BILLBAL_' + acct.accountId, 'tallyBlueLight');
			dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyBlueHL');
			dojo.removeClass('BILLBAL_' + acct.accountId, 'tallyRedHL');
		}
	}
}

function showUploadDialog() {
	dijit.byId("uploadDialog").show();
}

function processUpload(jsonData) {
	showMessage('msgUpload', jsonData.code, jsonData.message);
	if (jsonData.code == 0) {
		reloadEntryData();
	}
}

function changeDataKey() {
	var dKey = dijit.byId('dataKey').get('value');
	dojo.byId('dKey').value = dKey;

	var form = dojo.byId("tallyForm");
	form.method = 'post';
	form.action = APP_PATH + '/servlet/access/datakey';
	form.submit();
}

function populateBills(data) {
	var acctDesc = '';
	require(["dojo/dom-construct", "dojo/_base/array"], function(domConstruct, array) {
		//Replace all rows with the table header.
		domConstruct.place(dojo.byId("billHead"), "billsList", "only");

		array.forEach(data, function(b, i) {
			acctDesc = b.accountDesc;
			var c1 = b.dueDateWarning?"tallyRedHL":"tallyBlueHL";
			var c2 = b.open?"bulbGreen":(b.billBalance > 0 ?"bulbRedHL":"bulbRed");
			var f1 = b.billBalance > 0 ? "onclick='showBillPayDialog("+b.billId+")'":"";
			var r = "<tr style='height: 30px' valign='middle'><td align='center' class='"+c1+"' title='"+b.billId+"' onclick='javascript:listExpenses(0,false,"+b.billId+")'>"+b.fbillDt+"</td><td align='center' class='tallyBlueLight'>"+b.fdueDt+"</td><td align='right' class='tallyBlueLight'>"+b.fbillAmt+"</td><td align='right' class='tallyBlueLight'>"+b.fbillBalance+"</td><td align='center' class='tallyBlueLight'>"+b.fbillPaidDt+"</td><td align='center' class='tallyBlueLight'><div style='position: relative; top: 4px; left: 4px; width: 30px; height: 24px;' class='"+c2+"' "+f1+"></div></td></tr>";
			var row = domConstruct.toDom(r);
			domConstruct.place(row, "billsList");
		});
	});
	dojo.byId('b_acctDec').innerHTML = acctDesc;
}

function showBillPayDialog(billId) {
	fetchBillPayData(billId);
}

function populateBillPayForm(bui) {
	dijit.byId('bp_toAccount').set('value', bui.accountDesc);
	var tgt = dijit.byId('bp_toBillId');
	//Clear out all past bills.
	tgt.removeOption(tgt.getOptions());

	var val = bui.billId + '';
	var labl = bui.fbillDt + '  #' + bui.fbillId;
	var sel = 'true';
	tgt.addOption({value : val, selected : sel, label : labl});
	tgt.set('value', bui.billId);
}

function hideBillPayDialog() {
	dijit.byId("billPayDialog").hide();
	reloadEntryDataRetainBills();
}

function checkBillPay() {
	var form = dojo.byId('billPayForm');
	var profile = {
		required : ["bp_fromAccountId", "bp_transDate" ]
	};

	var results = dojox.validate.check(form, profile);
	if (results.isSuccessful()) {
		saveBillPay();
	} else {
		showMessage('msgBillPay', '1000', ERROR_INCOMPLETE);
	}
}

// ******************************************************************************************
// ************************************** Summary Page **************************************

function loadSummaryContent(data) {
	dojo.byId('pageNum').value = data.pageNum;

	CURR_MONTHS = data.months;

	// Headers
	for (var i = 0; i < PAGE_SIZE; i++) {
		var monthlabel = dojo.byId('month_' + i);
		var monthtotal = dojo.byId('monthtotal_' + i);

		// Blank out
		monthlabel.innerHTML = '';
		dojo.removeClass(monthlabel, 'sum-headerGreen');
		dojo.removeClass(monthlabel, 'sum-headerBlue');

		monthtotal.innerHTML = '';
		dojo.attr(monthtotal, 'title', '');
		dojo.removeClass(monthtotal, 'sum-headerBlueMed');
		dojo.removeClass(monthtotal, 'sum-headerBlueHL');

		if (i < data.months.length) {
			monthlabel.innerHTML = data.months[i].desc;
			var total = data.totals[data.months[i].seq];
			if (total) {
				monthtotal.innerHTML = total.fAmount;
				dojo.attr(monthtotal, 'title', total.count);
			} else {
				monthtotal.innerHTML = '0.00';
				dojo.attr(monthtotal, 'title', '0');
			}

			if (data.months[i].type == 'Y') {
				dojo.addClass(monthlabel, 'sum-headerBlue');
				dojo.addClass(monthtotal, 'sum-headerBlueMed');
			} else {
				dojo.addClass(monthlabel, 'sum-headerGreen');
				dojo.addClass(monthtotal, 'sum-headerBlueHL');
			}
		}
	}

	// Data
	for (var i = 0; i < data.categories.length; i++) {
		var catseq = data.categories[i].categoryId;
		for (var j = 0; j < PAGE_SIZE; j++) {
			var datafield = dojo.byId('data_' + catseq + '_' + j);

			// Blank out
			datafield.innerHTML = '';
			dojo.attr(datafield, 'title', '');
			dojo.removeClass(datafield, 'sum-dataBlue');
			dojo.removeClass(datafield, 'sum-dataGreyLight');

			if (j < data.months.length) {
				var ui = data.data[catseq] ? data.data[catseq][data.months[j].seq] : null;
				if (ui) {
					datafield.innerHTML = ui.fAmount;
					dojo.attr(datafield, 'title', ui.count);
					// var html = ui.fAmount;
					// if (ui.count > 1) {
					// html = html + '&nbsp;<sup>' + ui.count + '</sup>';
					// }
					// datafield.innerHTML = html;
				} else {
					datafield.innerHTML = '0.00';
					dojo.attr(datafield, 'title', '0');
				}

				if (data.months[j].type == 'Y') {
					dojo.addClass(datafield, 'sum-dataBlue');
				} else {
					dojo.addClass(datafield, 'sum-dataGreyLight');
				}
			}
		}
	}
}

function drillDown(month, categoryId) {
	dojo.byId('strTransMonth').value = CURR_MONTHS[month].fTransMonth;
	dojo.byId('categoryId').value = categoryId;

	if (dijit.byId('adhoc').get('checked') && !dijit.byId('regular').get('checked')) {
		dojo.byId('adhocInd').value = 'Y';
	} else if (!dijit.byId('adhoc').get('checked') && dijit.byId('regular').get('checked')) {
		dojo.byId('adhocInd').value = 'N';
	}

	var form = dojo.byId("summaryForm");
	form.method = 'post';
	form.action = APP_PATH + '/servlet/search/drill';
	form.submit();
}

// *****************************************************************************************
// ************************************** Search Page **************************************

function initSearchFields() {
	if (dojo.byId('category').value > 0) {
		dijit.byId('categoryId').set('value', dojo.byId('category').value);
	}

	if (dojo.byId('adhoc').value == 'Y' || dojo.byId('adhoc').value == 'N') {
		dijit.byId('adhocInd').set('value', dojo.byId('adhoc').value);
	}

	if (dojo.byId('adjust').value == 'Y' || dojo.byId('adjust').value == 'N') {
		dijit.byId('adjustInd').set('value', dojo.byId('adjust').value);
	}

	if (dojo.byId('transMon').value > 0) {
		dijit.byId('strTransMonth').set('value', dojo.byId('transMon').value);
	}
}

// *****************************************************************************************
// ************************************ Delete Expense *************************************

function showDeleteTransDialog() {
	var grid = dijit.byId('searchgrid');
	var items = grid.selection.getSelected();
	if (items && items.length > 0) {
		dojo.byId("tId").value = grid.store.getValue(items[0], 'transId');
		dijit.byId("deleteTransDialog").show();
	} else {
		showMessage('msg', '1000', SELECT_ITEM);
	}
}

function showDeleteTransDialog2(transId) {
	dojo.byId("tId").value = transId;
	dijit.byId("deleteTransDialog").show();
}

// *****************************************************************************************
// ************************************ Modify Expense *************************************

function showModifyTransDialog() {
	var grid = dijit.byId('searchgrid');
	var items = grid.selection.getSelected();
	if (items && items.length > 0) {
		dojo.byId("tId").value = grid.store.getValue(items[0], 'transId');
		fetchModifyTransData();
	} else {
		showMessage('msg', '1000', SELECT_ITEM);
	}
}

function showModifyTransDialog2(transId) {
	dojo.byId("tId").value = transId;
	fetchModifyTransData();
}

function populateModifyForm(tui) {
	dojo.byId("mod_transId").value = tui.transId;
	dijit.byId('m_adjustInd').set('checked', (tui.adjustInd == 'Y'));
	dijit.byId('m_adhocInd').set('checked', (tui.adhocInd == 'Y'));
	dijit.byId('m_fromAccountId').set('value', tui.fromAccountId);
	dijit.byId('m_toAccountId').set('value', tui.toAccountId);
	dijit.byId('m_categoryId').set('value', tui.categoryId);
	dijit.byId('m_description').set('value', tui.description);
	dijit.byId('m_amount').set('value', tui.amount);
	dijit.byId('m_transDate').set('value', tui.f2TransDate);

	dojo.byId('m_fromBillIdTran').value = tui.fromBillId;
	dojo.byId('m_toBillIdTran').value = tui.toBillId;
}

function checkModifyTrans() {
	var form = dojo.byId('modifyTransForm');
	var profile = {
		trim : [ "m_description" ],
		required : [ "m_description", "m_amount", "m_transDate" ]
	};

	var flag = false;
	if (dijit.byId('m_adjustInd').get('checked')) {
		flag = true;
	} else if (!dijit.byId('m_adjustInd').get('checked') && dijit.byId('m_categoryId').get('value') > 0) {
		flag = true;
	}

	var results = dojox.validate.check(form, profile);

	if (flag && results.isSuccessful()) {
		saveModifyTrans();
	} else {
		showMessage('msgMod', '1000', ERROR_INCOMPLETE);
	}
}

function hideModifyTransDialog() {
	dijit.byId("modifyTransDialog").hide();

	if(dojo.byId("pageId").value == 'ENTRY') {
		reloadEntryDataRetainFilter();
	} else {
		excuteSearch();
	}
}

function toggleAdjAdd() {
	toggleAdj('');
}

function toggleAdjMod() {
	toggleAdj('m_');
}

function loadFromBills() {
	var accId = dijit.byId('m_fromAccountId').get('value');
	fetchBillsList('from', accId);
}

function loadToBills() {
	var accId = dijit.byId('m_toAccountId').get('value');
	fetchBillsList('to', accId);
}

function populateModifyBills(src, bills) {
	var tgt = dijit.byId('m_'+src+'BillId');
	//Clear out all past bills.
	tgt.removeOption(tgt.getOptions());

	if(bills.length > 0) {
		tgt.set('disabled', false);
		tgt.addOption({value : 0, selected : 'false', label : ''});

		var selectedBill = (src=='from') ? dojo.byId("m_fromBillIdTran").value : dojo.byId("m_toBillIdTran").value;
		selectedBill = selectedBill + '';

		for (var i = 0; i < bills.length; i++) {
			var val = bills[i].billId + '';
			var labl = bills[i].fbillDt + '  #' + bills[i].fbillId;
			var sel = (selectedBill == bills[i].billId) ? 'true' : 'false';
			tgt.addOption({value : val, selected : sel, label : labl});
		}
		tgt.set('value', selectedBill);
	} else {
		tgt.set('disabled', true);
	}
}

function moveTransSeq(idx, code) {
	// CODE 0 = UP, 1 = DOWN.
	var grid = dijit.byId(GRID_ID);
	var store = grid.store;
	store.fetch( { onComplete : getGridSize } );

	var item_1 = store.getValue(grid.getItem(idx),'transId');

	//Invalid 'SWAP' combinations
	if((code == 0 && idx == 0) || (code == 1 && idx == (GRID_SIZE - 1))) {
		return false;
	}

	var idx2 = (code == 0) ? idx - 1 : idx + 1;
	var item_2 = store.getValue(grid.getItem(idx2),'transId');

	swapTransSeq(item_1, item_2);
}

var GRID_SIZE = 0;
var getGridSize = function(items, request) {
	GRID_SIZE = items.length;
};
