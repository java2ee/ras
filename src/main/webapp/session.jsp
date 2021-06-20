<%@page import="ru.funsys.util.Utils"%>
<%@page import="ru.funsys.avalanche.ras.station.BeanShell"%>
<%@page import="ru.funsys.avalanche.ras.BeanSession"%>
<%@page import="ru.funsys.avalanche.Messages"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<jsp:useBean id='server' scope='application' class='ru.funsys.avalanche.ras.Server'/>
<%
String name=request.getParameter("name");
String host=request.getParameter("host");
String id=request.getParameter("id");
String command=request.getParameter("command");
String close=request.getParameter("close");

BeanShell bean = null;
StringBuilder text = null;
boolean newSession = id == null ; 
if (!newSession) {
	bean = (BeanShell) session.getAttribute(id);
	text = (StringBuilder) session.getAttribute(id + "-text");
	newSession = bean == null;
}
if (newSession) {
	response.sendRedirect("create.jsp?host=" + host + "&name=" + name);
}
if (text == null) {
	text = new StringBuilder();
	session.setAttribute(id + "-text", text);
}
String error = null;
if (request.getMethod().equals("POST") && bean != null) {
	StringBuilder builder = new StringBuilder();
	
	if (close != null && close.length() > 0) {
		try {
			server.destroy(bean, name);			
		} catch (Exception e) {
			error = Utils.printStackTrace(e);
		}
	} else {
		try {
			String result = server.execute(bean, name, command);
			text.append(result);
		} catch (Exception e) {
			error = Utils.printStackTrace(e);
		}
		if (error == null) {
			session.setAttribute(bean.toString(), bean);
			response.sendRedirect("session.jsp?id=" + bean.toString() + "&name=" + name);
		}
		
	}

}
%>
<html>
<head>
<meta charset="UTF-8">
<title><%=server.getMessage("RAS10000", "ru", new Object[] {id})%></title>
</head>
<body>
<h1><%=server.getMessage("RAS10000", "ru", new Object[] {id})%></h1>
<table>
<tr><td colspan=""></td></tr>
<textarea id="sessionText" rows="30" cols="80"><%=text.toString() %></textarea>
<tr><td>
<form action="session.jsp" method="POST">
<input type="hidden" name="name" value="<%= name %>" />
<input type="hidden" name="id" value="<%= id %>" />
<table>
<tr>
<td><%=server.getMessage("RAS10107", "ru", new Object[] {id} )%><td>
<td><input type="text" name="command" value=""><td>
<td><button type="submit" name="enter"><%=server.getMessage("RAS10108", "ru")%></button><td>
</table>
</form>
</td></tr>
</table>
</body>
</html>