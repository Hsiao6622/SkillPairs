<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%
Blob image = null;
Connection con = null;
byte[] imgData = null;
Statement stmt = null;
ResultSet rs = null;

try {
	if ((boolean) request.getSession().getAttribute("login") == true) {
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillpairs", "root", "root");
		stmt = con.createStatement();
		rs = stmt.executeQuery(
		"select UserImg from member where UserID = '" + request.getSession().getAttribute("UID") + "'");
		if (rs.next()) {
	image = rs.getBlob(1);
	imgData = image.getBytes(1, (int) image.length());
		} else {
	out.println("Image Not Found");
	return;
		}
		// display the image
		response.setContentType("image/jpg");
		OutputStream o = response.getOutputStream();
		o.write(imgData);
		o.flush();
		o.close();
	} else {
		response.sendRedirect("HomeLogin.html");
	}
} catch (Exception e) {
	e.toString();
	return;
} finally {

	try {
		rs.close();
		stmt.close();
		con.close();

	} catch (SQLException e) {

		e.printStackTrace();

	}

}
%>