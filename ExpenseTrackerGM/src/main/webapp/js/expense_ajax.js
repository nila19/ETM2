
	//******************************************************************************************
	//*************************************** Entry Page ***************************************

	function addExpense() {
		var desc = dijit.byId('description').get('value');
		dojo.xhrPost({
			url: APP_PATH + '/servlet/entry/add',
			form: dojo.byId('entryForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msg', jsonData.code, jsonData.message);
		    	clearAddFields(desc);
		    	reloadEntryData();
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function tallyAccount(acctId) {
		dojo.xhrPost({
			url: APP_PATH + '/servlet/entry/tally/'+acctId,
			form: dojo.byId('entryForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msg', jsonData.code, jsonData.message);
		    	//reloadEntryDataRetainFilter();
		    	reloadAcctBal(acctId);
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function reloadTrans() {
		dojo.xhrPost({
			url: APP_PATH + '/servlet/entry/trans',
			form: dojo.byId('tallyForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		loadTransGrid('transgriddiv','transgrid','transId',jsonData.data, false);
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function reloadPFBal() {
		dojo.xhrGet({
			url: APP_PATH + '/servlet/entry/pfbalance',
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		refreshPFBal(jsonData.data);
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function reloadAcctBal(acct) {
		url1 = APP_PATH + '/servlet/entry/acbalance';
		if (acct != null) {
			url1 = url1 + '/'+acct;
		}
		dojo.xhrGet({
			url: url1,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		refreshAcctBal(jsonData.data);
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function reloadBills() {
		var acctId = dojo.byId('acctId').value;
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/getBillsforAc/'+acctId,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
			    	populateBills(jsonData.data);
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	//******************************************************************************************
	//************************************** Summary Page **************************************

	function loadSummary() {
		dojo.byId('foreCastInd').value = 'false';
		if(dijit.byId('foreCast').get('checked')) {
			dojo.byId('foreCastInd').value = 'true';
		}

		dojo.byId('adhocInd').value = '0';
		if(dijit.byId('adhoc').get('checked') && !dijit.byId('regular').get('checked')) {
			dojo.byId('adhocInd').value = 'Y';
		} else if(!dijit.byId('adhoc').get('checked') && dijit.byId('regular').get('checked')) {
			dojo.byId('adhocInd').value = 'N';
		}

		dojo.xhrPost({
			url: APP_PATH + '/servlet/summary/fetch',
			form: dojo.byId('summaryForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		loadSummaryContent(jsonData.data);
		    	}
		    },
			error: function() {
			},
			handle: function() {
			}
		});
	}

	function gotoSummaryPage(inc) {
		var page = dojo.byId('pageNum').value;

		dojo.xhrGet({
			url: APP_PATH + '/servlet/summary/paginate/'+inc+'/'+page,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		loadSummaryContent(jsonData.data);
		    	}
		    },
			error: function() {
			},
			handle: function() {
			}
		});
	}


	//******************************************************************************************
	//************************************** Search Page **************************************

	function excuteSearch() {
		if(!(dijit.byId('amount').get('value') >= 0)) {
			dijit.byId('amount').set('value', 0);
		}

		dojo.xhrPost({
			url: APP_PATH + '/servlet/search/execute',
			form: dojo.byId('searchForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
			    	loadTransGrid('searchgriddiv','searchgrid','transId',jsonData.data, true);
			    	loadTotal();
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	function loadTotal() {
		dojo.xhrGet({
			url: APP_PATH + '/servlet/search/total',
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
		    		dojo.byId('searchtotaldiv').innerHTML = jsonData.data;
		    	} else {
			    	showMessage('msg', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
			}
		});
	}

	//******************************************************************************************
	//************************************** Delete Trans **************************************

	function deleteTrans() {
		var transId = dojo.byId("tId").value;
		dojo.block("deleteTransDialog");
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/delete/'+transId,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msg', jsonData.code, jsonData.message);
		    	if(jsonData.code == 0) {
		    		if(dojo.byId("pageId").value == 'ENTRY') {
		    			reloadEntryDataRetainFilter();
		    		} else {
		    			excuteSearch();
		    		}
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("deleteTransDialog");
	    		dijit.byId("deleteTransDialog").hide();
			}
		});
	}

	//******************************************************************************************
	//************************************** Modify Trans **************************************

	function fetchModifyTransData() {
		var transId = dojo.byId("tId").value;
		dijit.byId("modifyTransDialog").show();
		dojo.block("modifyTransDialog");
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/get/'+transId,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
			    	populateModifyForm(jsonData.data);
		    	} else {
			    	showMessage('msgMod', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msgMod', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("modifyTransDialog");
			}
		});
	}

	function saveModifyTrans() {
		dojo.block("modifyTransDialog");
		dojo.xhrPost({
		    url: APP_PATH + '/servlet/entry/modify',
			form: dojo.byId('modifyTransForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msgMod', jsonData.code, jsonData.message);
		    },
			error: function() {
				showMessage('msgMod', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("modifyTransDialog");
			}
		});
	}

	function fetchBillsList(src, accId) {
		dojo.block("modifyTransDialog");
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/getBillsforAc/'+accId,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
			    	populateModifyBills(src, jsonData.data);
		    	} else {
			    	showMessage('msgMod', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msgMod', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("modifyTransDialog");
			}
		});
	}

	//******************************************************************************************
	//**************************************** Bill Pay ****************************************

	function fetchBillPayData(billId) {
		dijit.byId("billPayDialog").show();
		dojo.block("billPayDialog");
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/getBill/'+billId,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	if(jsonData.code == 0) {
			    	populateBillPayForm(jsonData.data);
		    	} else {
			    	showMessage('msgBillPay', jsonData.code, jsonData.message);
		    	}
		    },
			error: function() {
				showMessage('msgBillPay', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("billPayDialog");
			}
		});
	}

	function saveBillPay() {
		dojo.block("billPayDialog");
		dojo.xhrPost({
		    url: APP_PATH + '/servlet/entry/paybill',
			form: dojo.byId('billPayForm'),
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msgBillPay', jsonData.code, jsonData.message);
		    },
			error: function() {
				showMessage('msgBillPay', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("billPayDialog");
			}
		});
	}

	//******************************************************************************************
	//************************************** Swap Trans **************************************

	function swapTransSeq(transId_1, transId_2) {
		dojo.block("transgriddiv");
		dojo.xhrGet({
		    url: APP_PATH + '/servlet/entry/swapTransSeq/'+transId_1+'/'+transId_2,
		    handleAs: 'json',
		    load: function(jsonData) {
		    	showMessage('msg', jsonData.code, jsonData.message);
		    	if(jsonData.code == 0) {
		    		if(dojo.byId("pageId").value == 'ENTRY') {
		    			reloadEntryDataRetainFilter();
		    		} else {
		    			excuteSearch();
		    		}
		    	}
		    },
			error: function() {
				showMessage('msg', '1000', ERROR_COMMUNICATION);
			},
			handle: function() {
				dojo.unblock("transgriddiv");
			}
		});
	}
