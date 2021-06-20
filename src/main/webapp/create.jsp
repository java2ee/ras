<%@page import="ru.funsys.avalanche.ras.station.BeanShell"%>
<%@page import="ru.funsys.util.Utils"%>
<%@page import="ru.funsys.avalanche.ras.BeanSession"%>
<%@page import="ru.funsys.avalanche.Messages"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id='server' scope='application' class='ru.funsys.avalanche.ras.Server'/>
<%
String name=request.getParameter("name");
String host=request.getParameter("host");
String port=request.getParameter("port");
String user=request.getParameter("user");
String password=request.getParameter("password");
String error = null;
if (request.getMethod().equals("POST")) {
	StringBuilder builder = new StringBuilder();
	if (host == null || host.length() == 0) builder.append(Messages.getMessage("RAS0010E", "ru", null, true));
	if (port == null || port.length() == 0) builder.append(Messages.getMessage("RAS0011E", "ru", null, true));
	if (user == null || user.length() == 0) builder.append(Messages.getMessage("RAS0012E", "ru", null, true));
	if (password == null || password.length() == 0) builder.append(Messages.getMessage("RAS0013E", "ru", null, true));
	BeanShell bean = null;
	if (builder.length() == 0) {
		try {
			bean = server.create(name, host, port, user, password);
		} catch (Exception e) {
			error = Utils.printStackTrace(e);
		}
	} else {
		error = builder.toString();;
	}
	if (error == null) {
		session.setAttribute(bean.toString(), bean);
		response.sendRedirect("session.jsp?id=" + bean.toString() + "&name=" + name);
	}
}
if (host == null) host = "";
if (port == null) port = "22";
if (user == null) user = "";
%>
<html>
<head>
<meta charset="UTF-8">
<title><%=server.getMessage("RAS10100", "ru")%></title>
</head>
<body>
<h1><%=server.getMessage("RAS10100", "ru")%></h1>
<% if (error != null) { %>
<font color="red"><%= error %></font>
<% }%>
<form method="POST" action="create.jsp">
<input type="hidden" name="name" value="<%= name %>"/>
<table>
<tr><td align="right"><%= server.getMessage("RAS10101", "ru") %></td><td align="left"><input type="text" name="host" value="<%= host %>" /></td></tr>
<tr><td align="right"><%= server.getMessage("RAS10102", "ru") %></td><td align="left"><input type="text" name="port" value="<%= port %>" /></td></tr>
<tr><td align="right"><%= server.getMessage("RAS10103", "ru") %></td><td align="left"><input type="text" name="user" value="<%= user %>" /></td></tr>
<tr><td align="right"><%= server.getMessage("RAS10104", "ru") %></td><td align="left"><input type="password" name="password" value="" /></td></tr>
<tr><td align="right"><button type="submit"><%= server.getMessage("RAS10105", "ru") %></button></td><td align="left"><button type="reset"><%= server.getMessage("RAS10106", "ru") %></button></td></tr>
</table>
</form>

</body>
</html>