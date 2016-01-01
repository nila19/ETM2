//***************************************************************************************
//*********************************** Dashboard Page ************************************
var GRID_ID;
function loadTransGrid(divId, gridId, id, jsonData, selectable) {
	GRID_ID = gridId;
	if (dojo.byId(gridId)) {
		reloadGrid(gridId, id, jsonData);
	} else {
		var transstore = buildStore(id, jsonData);

		selectable = false;

		var translayout = [ {
			defaultCell : {
				width : "70px",
				cellStyles : "text-align: center; font: 11px Verdana, Geneva, Arial, Helvetica, sans-serif; padding: 2px; height: 24px;",
				headerStyles : "text-align: center; height: 27px; vertical-align: middle; font: 11px Verdana, Geneva, Arial, Helvetica, sans-serif; padding: 2px;" },
			cells : [ { field : 'transId', name : ' ', width : '70px', formatter : function (value, rowIndex, gridId) {
						return "<div style='position: relative; top: 0px; left: 0px; height: 16px;'>" +
						"<div style='position: relative; top: 0px; left: 2px; width: 16px; height: 16px;' title='Up' onclick='javascript:moveTransSeq("+rowIndex+",0);' class='dgUp'></div>" +
						"<div style='position: relative; top: -16px; left: 18px; width: 16px; height: 16px;' title='Down' onclick='javascript:moveTransSeq("+rowIndex+",1);' class='dgDown'></div>" +
						"<div style='position: relative; top: -32px; left: 34px; width: 16px; height: 16px;' title='Edit' onclick='javascript:showModifyTransDialog2("+value+");' class='dgEdit'></div>" +
						"<div style='position: relative; top: -48px; left: 50px; width: 16px; height: 16px;' title='Delete' onclick='javascript:showDeleteTransDialog2("+value+");' class='dgDelete'></div></div>";} },
					{ field : 'fentryDate', name : 'ENTRY DATE', width : '120px' },
					{ field : 'ftransDate', name : 'TRANS', width : '60px' },
					{ field : 'category', name : 'CATEGORY', width : '160px' },
					{ field : 'description', name : 'DESCRIPTION', width : '180px' },
					{ field : 'famount', name : 'AMT', width : '80px' },
					{ field : 'fromAccountDesc', name : 'FROM A/C', width : '110px' },
					{ field : 'fFromBalanceBf', name : 'FR $ &#8658;', width : '80px' },
					{ field : 'fFromBalanceAf', name : 'FR $', width : '80px' },
					{ field : 'toAccountDesc', name : 'TO A/C', width : '110px' },
					{ field : 'fToBalanceBf', name : 'TO $ &raquo;&raquo;', width : '80px' },
					{ field : 'fToBalanceAf', name : 'TO $', width : '80px' },
					{ field : 'flag', name : '&Psi;', width : '25px', formatter : function (value) {
						var s = "<div style='position: relative; top: 0px; left: 0px; height: 16px;'>";
						if(value == 'H'){s += "<div style='position: relative; top: 0px; left: 4px; width: 16px; height: 16px;' title='Adhoc' class='dgAdhoc'></div>";}
						if(value == 'J'){s += "<div style='position: relative; top: 0px; left: 4px; width: 16px; height: 16px;' title='Adjustment' class='dgAdj'></div>";}
						s += "</div>";
						return s;
						} } ] } ];

		var transgrid = null;
		if (selectable) {
			transgrid = new dojox.grid.EnhancedGrid({ id : gridId, query : { transId : '*' }, store : transstore,
				clientSort : true, structure : translayout, selectionMode : "single",
				canSort : true, selectable : true, plugins : { indirectSelection : true } }, document
					.createElement('div'));
		} else {
			transgrid = new dojox.grid.EnhancedGrid({ id : gridId, query : { transId : '*' }, store : transstore,
				clientSort : true, structure : translayout, selectionMode : "single",
				canSort : true, selectable : false }, document.createElement('div'));
		}

		dojo.byId(divId).appendChild(transgrid.domNode);
		transgrid.startup();
	}
}

// *************************************************************************************
// ********************************** Utility Methods
// **********************************

function formatNumber(amt) {
	if (amt == '') {
		return amt;
	} else {
		return dojo.number.format(amt);
	}
}

function formatDate(datum) {
	var d = dojo.date.stamp.fromISOString(datum);
	return dojo.date.locale.format(d, { selector : 'date', formatLength : 'long' });
}

// Utility method to refresh the Grid data without recreating the grid.
function reloadGrid(gridId, id, jsonData) {
	var newstore = buildStore(id, jsonData);
	dijit.byId(gridId).setStore(newstore);
}

function switchGrid(oldGridId, gridId) {
	dijit.byId(oldGridId).domNode.style.visibility = 'hidden';
	dijit.byId(oldGridId).domNode.style.height = 0;

	if (dijit.byId(gridId)) {
		dijit.byId(gridId).domNode.style.visibility = 'visible';
	}
}

function buildStore(id, jsonData) {
	var store = new dojo.data.ItemFileWriteStore({ identifier : id, data : { items : jsonData } });
	return store;
}

function itemToJSON(store, item) {
	var json = {};
	var attributes = store.getAttributes(item);
	var i;

	for (i = 0; i < attributes.length; i++) {
		var values = store.getValues(item, attributes[i]);
		// Handle multi-valued and single-valued attributes.
		if (values.length > 1) {
			var j;
			json[attributes[i]] = [];
			for (j = 0; j < values.length; j++) {
				var value = values[j];
				// Check that the value isn't another item. If it is, process it
				// as an item.
				if (store.isItem(value)) {
					json[attributes[i]].push(dojo.fromJson(itemToJSON(store, value)));
				} else {
					json[attributes[i]].push(value);
				}
			}
		} else {
			if (store.isItem(values[0])) {
				json[attributes[i]] = dojo.fromJson(itemToJSON(store, values[0]));
			} else {
				json[attributes[i]] = values[0];
			}
		}
	}
	return json;
}
