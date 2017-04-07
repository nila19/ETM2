var ERROR_COMMUNICATION = "Communication Error occured. Data is not saved.";
var ERROR_INCOMPLETE = "Mandatory information is missing in the form";
var SELECT_ITEM = "Please select an item from the list";

function showMessage(target, code, message) {
	hideMessage(target);
	dojo.addClass(dojo.byId(target), code == 0 ? "msgSuccess" : "msgError");
	dojo.byId(target).innerHTML = message;
	setTimeout('hideMessage(' + target + ')', 3000);
}

function hideMessage(target) {
	dojo.byId(target).innerHTML = '';
	dojo.removeClass(dojo.byId(target), "msgSuccess msgError");
}
