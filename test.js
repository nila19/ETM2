function populateList2(array) {

//I added some comments here...

	require(["dojo/dom-construct", "dojo/_base/array"], function(domConstruct, array) {
		//Replace all rows with the table header. some more changes.
		domConstruct.place(dojo.byId("empHead"), "empList", "only");

		array.forEach(data, function(b, i) {
			var r = "<tr style='height: 30px' valign='middle'><td align='center'>"+b.empID+"</td><td align='center' class='tallyBlueLight'>"+b.name+"</td><td align='right' class='tallyBlueLight'>"+b.role+"</td><td align='right' class='tallyBlueLight'>"+b.department+"</td><td align='center' class='tallyBlueLight'>"+b.joinDate+"</td></tr>";
			var row = domConstruct.toDom(r); //Very important
			domConstruct.place(row, "empList");
			return;
		});
	});
}
