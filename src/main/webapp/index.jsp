<%@page import="ru.funsys.avalanche.ras.BeanStation"%>
<%@ page import="java.util.List"%>
<%@ page import="ru.funsys.avalanche.ras.Server"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<body>
<h2>Remote access server</h2>
<jsp:useBean id='server' scope='application' class='ru.funsys.avalanche.ras.Server'/>
<%
List<BeanStation> list = server.getStations();
BeanStation[] beans = new BeanStation[list.size()];
list.toArray(beans);
for (int index = 0; index < beans.length; index++) {
	BeanStation bean = beans[index]; 
	out.print("<a href=\"session.jsp?host=" + bean.getHostName() + "&name=" + bean.getName() + "\">" + bean.getHostName() + "</a>");
	out.println("<br>");
}
%>
</body>
</html>
