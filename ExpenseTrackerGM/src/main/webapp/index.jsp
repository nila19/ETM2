<html>
<%@ page import="org.apache.commons.lang3.*" %>
<%@ page import="com.test.Tester" %>
<body>
<h2>Hello World!</h2>
<% 
	Tester t = new Tester(); 
%>

<%= t.add(10, 15) %>
<BR><BR>
<%= t.add("sdfsfd", "oiuiouoi") %>

</body>
</html>
