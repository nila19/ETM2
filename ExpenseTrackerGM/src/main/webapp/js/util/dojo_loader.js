	function loadDojo() {
		for (var i = 1; i < arguments.length; i++) {
			//Basic
			dojo.require("dojo.parser");
			dojo.require("dojo.number");
	        dojo.require("dijit.layout.BorderContainer");
	        dojo.require("dijit.layout.ContentPane");
	        dojo.require("dijit.layout.TabContainer");
		    dojo.require("dijit.Dialog");

			if(arguments[i] == 'form') {
				dojo.require("dijit.form.Form");
				dojo.require("dijit.form.TextBox");
				dojo.require("dijit.form.DateTextBox");
				dojo.require("dijit.form.CurrencyTextBox");
				dojo.require("dijit.form.ValidationTextBox");
				dojo.require("dijit.form.Button");
				dojo.require("dijit.form.CheckBox");
				dojo.require("dijit.form.Select");
				dojo.require("dijit.form.FilteringSelect");
				dojo.require("dijit.form.ComboBox");
			    dojo.require("dijit.form.Textarea");
			}
			if(arguments[i] == 'validate') {
				dojo.require("dojox.validate");
				dojo.require("dojox.validate.check");
			}
			if(arguments[i] == 'dialog') {
			    dojo.require("dijit.Dialog");
			    dojo.require("dijit.Tooltip");
			}
			if(arguments[i] == 'grid') {
			    dojo.require("dojox.grid.DataGrid");
			    dojo.require("dojox.data.CsvStore");
			    dojo.require("dojox.grid.EnhancedGrid");
			    dojo.require("dojox.grid.enhanced.plugins.IndirectSelection");
			    dojo.require("dojo.data.ItemFileReadStore");
			    dojo.require("dojo.data.ItemFileWriteStore");
			}
			if(arguments[i] == 'chart') {
				dojo.require("dojox.charting.Chart2D");
				dojo.require("dojox.charting.widget.Legend");
				dojo.require("dojox.charting.widget.SelectableLegend");
				dojo.require("dojox.charting.action2d.Tooltip");
				dojo.require("dojox.charting.action2d.Magnify");
				dojo.require("dojox.charting.action2d.MoveSlice");
				dojo.require("dojox.charting.action2d.Highlight");
				dojo.require("dojox.charting.themes.Claro");
				dojo.require("dojox.charting.themes.Julie");
				dojo.require("dojox.charting.themes.MiamiNice");
				dojo.require("dojox.charting.themes.PlotKit.blue");
			}
			if(arguments[i] == 'gauge') {
		    	dojo.require('dojox.gauges.AnalogGauge');
		    	dojo.require('dojox.gauges.AnalogArcIndicator');
		    	dojo.require('dojox.gauges.TextIndicator');
				dojo.require("dojox.gauges.BarGauge");
				dojo.require('dojox.gauges.BarIndicator');
			}
			if(arguments[i] == 'editor') {
				dojo.require("dijit.Editor");
				dojo.require("dijit._editor.plugins.TextColor");
				dojo.require("dijit._editor.plugins.LinkDialog");
				dojo.require("dijit._editor.plugins.FullScreen");
				dojo.require("dijit._editor.plugins.ViewSource");
			}
			if(arguments[i] == 'slider') {
		        dojo.require("dijit.form.HorizontalSlider");
		        dojo.require("dijit.form.HorizontalRuleLabels");
		        dojo.require("dijit.form.HorizontalRule");
			}
			if(arguments[i] == 'upload') {
		        dojo.require("dojox.form.Uploader");
		        dojo.require("dojox.form.uploader.FileList");
		        dojo.require("dojox.form.uploader.plugins.HTML5");
			}
		}
	}

	function resizeDojoInputs(list) {
		for (var i = 0; i< list.length; i++) {
			var row = list[i];
			if(dojo.byId(row[0])) {
				dojo.style(dijit.byId(row[0]).domNode, "width", row[1]);
			}
		}
	}

	function hideDojoInputs(list) {
		for (var i = 0; i< list.length; i++) {
			var row = list[i];
			if(dojo.byId(row)) {
				dojo.style(dijit.byId(row).domNode, 'display', 'none');
			}
		}
	}

	function freezeDojoInputs(djs) {
        for(var i = 0; i < djs.length; i++) {
        	if(dojo.byId(djs[i])) {
        		dijit.byId(djs[i]).set('readonly', true);
        	}
        }
	}

	//Utility methods to modify 'Dojo Validation Profile'
	function addTrim(profile, fields) {
		dojo.forEach(fields, function(field, index) {
			profile.trim.push(field);
		});
	}

	function addRequired(profile, fields) {
		dojo.forEach(fields, function(field, index) {
			profile.required.push(field);
		});
	}

	function addConstraints(profile, fields) {
		dojo.forEach(fields, function(field, index) {
        	if(dojo.byId(field)) {
        		profile.constraints[field] = [dojox.validate.isInRange, 0, 9999999999];
        	}
		});
	}

	function checkPctFormats(fields) {
		dojo.forEach(fields, function(field, index) {
        	if(dojo.byId(field)) {
	        	if(isNaN(dijit.byId(field).get('value'))) {
	        		return false;
	        	}
        	}
		});
        return true;
	}

	function makeFormReadonly(frm) {
		dojo.query("input, button, textarea, select", dojo.byId(frm)).attr("disabled", true);
		dojo.query("input, button, textarea, select", dojo.byId(frm)).style({color: 'grey'});
	}

	function disableDijits(djs) {
        for(var i = 0; i < djs.length; i++) {
        	if(dojo.byId(djs[i])) {
        		dijit.byId(djs[i]).set('disabled', true);
        	}
        }
	}

	function setDijitValue(item, value) {
		if(dojo.byId(item)) {
			dijit.byId(item).set('value', value);
		}
	}

	function nolink() {
	}

    function reloadPage() {
    	window.location.reload();
    }

    function gotoPage(url) {
    	window.location.href = url;
    }

	function showFaq() {
		dijit.byId("faqDialog").show();
	}
