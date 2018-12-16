<%--
  Created by IntelliJ IDEA.
  User: LinTan
  Date: 2018/11/22
  Time: 12:38
  To change this template use File | Settings | File Templates.
--%>
<%
    response.sendRedirect(request.getContextPath() + "/computerServlet?method=getComputers");
%>